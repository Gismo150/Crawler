package main;

import Models.BuildSystem;
import Models.RMetaData;
import com.google.common.util.concurrent.RateLimiter;
import org.eclipse.egit.github.core.*;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.client.PageIterator;
import org.eclipse.egit.github.core.service.CommitService;
import org.eclipse.egit.github.core.service.ContentsService;
import org.eclipse.egit.github.core.service.RepositoryService;
import org.eclipse.egit.github.core.service.UserService;
import utils.JsonWriter;
import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Main GitHub Crawler class. Queries, filters and stores the GitHub repositories.
 *
 * @author Daniel Braun
 */
public class GitHubCrawler {
    /**
    * The filtered programming language.
    */
    private String searchLanguage;
    /**
     * The BuildSystem to detect and filter for.
     */
    private BuildSystem buildSystem;
    /**
     * The GitHub client object.
     */
    private GitHubClient client;
    private String lastPushedDate;
    private int maxStars = Integer.MAX_VALUE;
    private int starDecreaseAmount;
    private int matchingRepos = 0;
    private int checkedRepos = 0;
    private int counterSearchRequests = 0;
    private int counterRepositoryRequests = 0;
    private int counterContentRequests = 0;
    private int counterCommitRequests = 0;
    private boolean foundRepoInLastQuery;
    private boolean notFirstQuery = false;
    private RepositoryService repositoryService;
    private CommitService commitService;
    private ContentsService contentsService;
    // Request throttling using the com.google.guava 28.0-jre library
    // SEE: https://www.javadoc.io/doc/com.google.guava/guava/28.0-jre/com/google/common/util/concurrent/RateLimiter.html
    private RateLimiter requestRateLimiter;
    private RateLimiter searchRequestRateLimiter;
    private Logger logger;
    private long startTime;
    private int localRemainingRequests = 5000;
    private double maximumWaitingTime = 0;
    private double overallWaitingTime = 0;
    /**
     * Crawlers Constructor.
     * @param language The programming language filter.
     * @param buildSystem The build system filter.
     * @param oAuthToken The Github OAuth token for authentication.
     */
    public GitHubCrawler(String language, String lastPushedDate, String starsDecreaseAmount , BuildSystem buildSystem, String oAuthToken, Logger logger, long startTime){
        this.logger = logger;
        this.startTime = startTime;
        this.searchLanguage = language;
        this.lastPushedDate = lastPushedDate;
        this.buildSystem = buildSystem;
        this.client = authenticate(oAuthToken);
        repositoryService = new RepositoryService(client);
        commitService = new CommitService(client);
        contentsService = new ContentsService(client);

        //Request limit values are defined here : https://developer.github.com/v3/#rate-limiting
        //Search Request limit values are defined here: developer.github.com/v3/search/#rate-limit
        if(Config.OAUTHTOKEN.equals("")) {
            requestRateLimiter = RateLimiter.create(60d/3600d);
            searchRequestRateLimiter = RateLimiter.create(10d/60d);
        } else { // assuming correct token was provided!
            UserService userService = new UserService(client);
            try {
                if (userService.getUser() != null) { //Check if current user is correctly authenticated.
                    System.out.println("Your current remaining request limit is: " + client.getRemainingRequests());
                    logger.info("Current remaining request limit is: " + client.getRemainingRequests());
                    requestRateLimiter = RateLimiter.create((double) client.getRemainingRequests() / 3600d);
                }

            } catch (IOException e) {
                System.err.println(e.getMessage());
                System.exit(1);
            }
            //Not possible to request the remaining amount of search requests, thus it is set to the maximum
            // for authenticated users.
            searchRequestRateLimiter = RateLimiter.create(30d/60d);
        }

        System.out.println("Requests are throttled to " + requestRateLimiter.getRate() + " requests per second.");
        logger.info("Requests are throttled to " + requestRateLimiter.getRate() + " requests per second.");
        System.out.println("Search requests are throttled to " + searchRequestRateLimiter.getRate() + " requests per second.");
        logger.info("Search requests are throttled to " + searchRequestRateLimiter.getRate() + " requests per second.");

        try {
            this.starDecreaseAmount = Integer.parseInt(starsDecreaseAmount);
            if(starDecreaseAmount <= 0){
                System.err.println("starsDecreaseAmount must be greater 0. Config file not properly set up.\nShutting down.");
                System.exit(1);
            }
        } catch (NumberFormatException e) {
            System.err.println("starsDecreaseAmount is not an integer. Config file not properly set up.\nShutting down.");
            System.exit(1);
        }
    }

    /**
     * The main entry point to start the crawler.
     */
    public void run() {
        while(true) {
            filterRepositories(buildSearchQuery());
        }
    }

    /**
     * Function to authenticate to the GitHub client. Authenticated user have 5000 request per hour.
     * Not authenticated users have 60 requests per hour.
     *
     * @param oAuthToken The Github OAuth token for authentication.
     * @return Either an authenticated ot not authenticated GithubClient.
     */
    private GitHubClient authenticate(String oAuthToken) {
        GitHubClient client = new GitHubClient();
        try {
            client.setOAuth2Token(oAuthToken);
        } catch (Exception e) {
            System.err.println("Authentication failed!");
            System.err.println(e.getMessage());
        }
        return client;
    }

    private Map<String, String> buildSearchQuery() {
        Map<String, String> searchQuery = new HashMap<String, String>();
        searchQuery.put("language", searchLanguage); //Search for repos with given searchlLanguage set in the config file
        searchQuery.put("is", "public"); //Search for repos that are public
        searchQuery.put("pushed", ">=" + lastPushedDate); // The pushed qualifier will return a list of repositories, sorted by the most recent commit made on any branch in the repository.
        searchQuery.put("sort", "stars");

        if(maxStars != Integer.MAX_VALUE && maxStars > 0 && foundRepoInLastQuery) { // && foundRepoInLastQuery
            maxStars = maxStars - 1; // mir entfallen nur die conan proj die von der letzten query genau maxStars hatten und hinter den 1000 results lagen
            System.out.println("Querying repositories with maximum number of stars of '" + maxStars + "' from last repository of previous query.");
            searchQuery.put("stars", "<=" + (maxStars));
        } else if(!foundRepoInLastQuery && maxStars != Integer.MAX_VALUE) { //
            System.out.println("No repository was found within the last 1000 crawled repositories.\nDecreasing the current stars count of "+maxStars+" by "+starDecreaseAmount+".");
            maxStars = maxStars - starDecreaseAmount;
            searchQuery.put("stars", "<=" + maxStars);
        } else if(!foundRepoInLastQuery && maxStars == Integer.MAX_VALUE && notFirstQuery) {
            //NOTE: This case is ignored. If we do not find any popular repository within the first 1000 repositories that uses conan,
            //then it would be unnecessary to crawl either for conan or on github for it.
            System.err.println("No popular repository was found within the first query without a stars limit.\nShutting Down.");
            logger.info("No popular repository was found within the first query without a stars limit.\nShutting Down.");
            System.err.println("NOTE: This case is ignored. If we do not find any popular repository that uses conan within the first 1000 repositories, " +
                    "then it would be unnecessary to crawl either for conan or on github for it.");
            System.exit(1);
        }
        notFirstQuery = true;
        foundRepoInLastQuery = false;
        if(maxStars <= 0) {
            //including 0 otherwise there is no other termination, due to the case that when the stars count reaches 0 and the query finds repositories,
            // it will set the stars count again to 0, resulting to the same query in a loop.
            System.out.println("Minimum value for stars reached. | STARS COUNT: " + maxStars);
            System.out.println("Crawling finished.\nShutting down");
            logger.info("Overall amount of checked repositories: " + checkedRepos);
            logger.info("Overall amount of matching repositories found: " + matchingRepos);
            logger.info("Amount of search requests sent: " + counterSearchRequests);
            logger.info("Amount of repository requests sent: " + counterRepositoryRequests);
            logger.info("Amount of content requests sent: " + counterContentRequests);
            logger.info("Amount of commit requests sent: " + counterCommitRequests);
            logger.info("Remaining requests before termination: " + localRemainingRequests);
            logger.info("Maximum time spent waiting before any request in seconds: " + maximumWaitingTime);
            logger.info("Overall time spent waiting before any request in seconds: " + overallWaitingTime);
            logger.info("Overall time spent waiting before any request in minutes: " + overallWaitingTime/60);
            logger.info("Overall time spent waiting before any request in hours: " + overallWaitingTime/3600);

            long endTime   = System.nanoTime();
            long duration = endTime - startTime;
            logger.info("Crawler started at: " + new Date(startTime).toString());
            logger.info("Crawler terminated at" + new Date(endTime).toString());
            logger.info("Overall execution time in seconds: " + TimeUnit.NANOSECONDS.toSeconds(duration));
            logger.info("Overall execution time in minutes: " + TimeUnit.NANOSECONDS.toSeconds(duration)/60);
            logger.info("Overall execution time in hours: " + TimeUnit.NANOSECONDS.toSeconds(duration)/3600);
            logger.info("Crawling finished. Shutting down");
            System.exit(0);
        }
        return searchQuery;
    }

    private List<SearchRepository> queryRepositories(Map<String, String> searchQuery, int page){
        try {
            //search requests also count as a general request and thus are also throttled
            //by the general request limiter.
            double wait1 = requestRateLimiter.acquire();
            double wait2 = searchRequestRateLimiter.acquire();
            updateMaxWaitingTime(wait1);
            updateMaxWaitingTime(wait2);
            counterSearchRequests++;
            return repositoryService.searchRepositories(searchQuery, page);
        } catch (IOException e) {
            System.err.println("Something went wrong while performing the repository search request.\nAborting.\n");
            System.err.println(e.getMessage());
            System.exit(1);
        }
        return null;
    }

    private Repository queryRepoByOwnerAndName(SearchRepository searchRepository) {
        try {
            double wait = requestRateLimiter.acquire();
            updateMaxWaitingTime(wait);
            counterRepositoryRequests++;
            return repositoryService.getRepository(searchRepository.getOwner(), searchRepository.getName());
        } catch(IOException e) {
            System.err.println("Something went wrong while getting the Repository by Owner and repository Name. Skipping to next repository.");
            System.err.println(e.getMessage());
        }
        return null;
    }

    private void filterRepositories(Map<String, String> searchQuery) {

        for (int page = 1; page <= 10; page++) {

            List<SearchRepository> searchRepositoryResponse = queryRepositories(searchQuery, page);

            if (searchRepositoryResponse.isEmpty()) { // If we reached a page number that returns no repositories (empty list) in the query.
                System.out.println("Found " + searchRepositoryResponse.size() + " Repos by search at page " + page);
                System.out.println("Crawling Finished.\nShutting down.");
                System.exit(0);
                break;
            } else {

                System.out.println("Query Response:\nNumber Repos: " + searchRepositoryResponse.size() + "\nOn page " + page + ".\n");

                for (SearchRepository searchRepository : searchRepositoryResponse) {

                    Repository repositoryOfOwnerAndName = queryRepoByOwnerAndName(searchRepository);
                    if (repositoryOfOwnerAndName != null) {
                        maxStars = repositoryOfOwnerAndName.getWatchers();
                        System.out.println("-----------------------------------------------");
                        System.out.println("Current maximum stars count: " + maxStars);
                        checkedRepos++;
                        //Detect BuildSystem subroutine
                        BuildSystem foundBuildSystem = getFileContentsAtRootDir(repositoryOfOwnerAndName); // detectBuildSystem(repositoryOfOwnerAndName);
                        if (foundBuildSystem == buildSystem) { //BuildSystem was detected. Create a new RMetaData object and store all information
                            System.out.println("-----------------------------------------------");
                            matchingRepos++;
                            foundRepoInLastQuery = true;
                            System.err.println("Overall detected repos: " + matchingRepos);
                            RMetaData metaDataObject = createRMetaDataObject(repositoryOfOwnerAndName, foundBuildSystem);
                            JsonWriter.getInstance().writeRepositoryToJson(metaDataObject);
                        }
                        System.out.println("Request Limit: " + client.getRequestLimit());
                        System.out.println("Remaining Request: " + client.getRemainingRequests());
                        if(localRemainingRequests > client.getRemainingRequests()) {
                            localRemainingRequests = client.getRemainingRequests();
                        } else {
                            logger.info("Remaining requests before reset: " + localRemainingRequests);
                            long resetTime   = System.nanoTime();
                            logger.info("Requests reset at: " + new Date(resetTime).toString());
                            localRemainingRequests = 5000;
                        }

                    }
                }
            }
        }
        System.out.println("Maximum number of 1000 repositories were processed within one search query.\nSkipping others due to limitation.");
    }

    private RMetaData createRMetaDataObject(Repository repository, BuildSystem buildSystem) {
        RMetaData meteDataObject = new RMetaData();
        //Set all crawled fields
        meteDataObject.setId(repository.getId());
        meteDataObject.setName(repository.getName());
        meteDataObject.setOwner(repository.getOwner().getLogin());
        meteDataObject.setOwnerType(repository.getOwner().getType());
        meteDataObject.setDescription(repository.getDescription());
        meteDataObject.setLanguage(repository.getLanguage());
        meteDataObject.setHasDownloads(repository.isHasDownloads());
        meteDataObject.setSize(repository.getSize());
        meteDataObject.setPushedAt(repository.getPushedAt());
        meteDataObject.setCreatedAt(repository.getCreatedAt());
        meteDataObject.setDefaultBranch(repository.getMasterBranch());
        meteDataObject.setLatestCommitId(getLatestCommitId(repository));
        meteDataObject.setPrivate(repository.isPrivate());
        meteDataObject.setForksCount(repository.getForks());
        meteDataObject.setOpenIssuesCount(repository.getOpenIssues());
        meteDataObject.setStargazersCount(repository.getWatchers()); // NOTE: stargazers and watchers count are the same since 2012.
                                                                     // The matchingRepos now only increases if a project is starred.
                                                                     // SEE https://developer.github.com/changes/2012-09-05-watcher-api/
        meteDataObject.setHtmlUrl(repository.getHtmlUrl());
        meteDataObject.setCloneUrl(repository.getCloneUrl());
        meteDataObject.setBuildSystem(buildSystem.toString());
        meteDataObject.setBuildFilePath(buildSystem.getFilePaths());
        //Setting default values
        meteDataObject.setBuildStatus("UNKNOWN");
        meteDataObject.setErrorMessage(new ArrayList<>());
        meteDataObject.setPackageDependencies(new ArrayList<>());

        return meteDataObject;
    }

    /**
     * Detects if the repository contains specific build files required by the currently searched build system.
     *
     * @param repository The repository to detect the build system from
     * @return The detected BuildSystem
     */
    private BuildSystem getFileContentsAtRootDir(Repository repository) {
        BuildSystem detectedBuildSystem = BuildSystem.UNKNOWN;
        List<String> filePaths = new ArrayList<>();

        try {
            double wait = requestRateLimiter.acquire();
            updateMaxWaitingTime(wait);
            List<RepositoryContents> repositoryContents = contentsService.getContents(repository);
            counterContentRequests++;
            switch (buildSystem) { //using switch as it is easy to extend with further buildsystems to detect by adding more custom cases.
                case CMAKE:
                    if((repositoryContents.stream().anyMatch(o -> o.getName().equals("conanfile.py"))
                        || repositoryContents.stream().anyMatch(o -> o.getName().equals("conanfile.txt")))
                        && repositoryContents.stream().anyMatch(o -> o.getName().equals("CMakeLists.txt"))) { //When checking for the existence of a CMakeLists.txt file,
                                                                                                              //the [generator] defined in the conanfile will most likely be set to CMake
                                                                                                              //Thus we make an assumption that the generator is always set to CMake in the conanfile!
                        if(repositoryContents.stream().anyMatch(o -> o.getName().equals("conanfile.py")))
                            filePaths.add("conanfile.py");
                        else
                            filePaths.add("conanfile.txt");
                        detectedBuildSystem = BuildSystem.CMAKE;
                    }
                    break;
            }
        } catch (IOException e) {
            System.err.println("Something went wrong while querying the repository contents.\n");
            System.err.println(e.getMessage());
        }
        detectedBuildSystem.setFilePaths(filePaths);
        return detectedBuildSystem;
    }

    /**
     * Gets the latest commit id (sha) from the repository default (master) branch.
     *
     * @param repository The repository we are currently looking at.
     * @return The latest commit id as a String.
     */
    private String getLatestCommitId(Repository repository){
        double wait = requestRateLimiter.acquire();
        updateMaxWaitingTime(wait);
        PageIterator<RepositoryCommit> repositoryCommitList = commitService.pageCommits(repository, 1);
        counterCommitRequests++;
        if(repositoryCommitList.hasNext())
            return repositoryCommitList.next().iterator().next().getSha();
        else return "";
    }

    private void updateMaxWaitingTime(double waitingTime) {
        if(maximumWaitingTime < waitingTime) {
            maximumWaitingTime = waitingTime;
        }
        overallWaitingTime += waitingTime;
    }
}
