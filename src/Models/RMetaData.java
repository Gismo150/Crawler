package Models;

import java.util.Date;
import java.util.List;

/**
 * Repository MetaData Model that is stored into the XML database.
 *
 * @author Daniel Braun
 */
public class RMetaData {

    // SET by the Crawler
    private long id;
    private String name;
    private String owner;
    private String ownerType;
    private String description;
    private String language;
    private boolean hasDownloads;
    private int size;
    private Date createdAt;
    private Date pushedAt;
    private String defaultBranch;
    private String latestCommitId;
    private boolean isPrivate;
    private int forksCount;
    private int openIssuesCount;
    private int stargazersCount;
    private String htmlUrl;
    private String cloneUrl;
    private String buildSystem;
    private List<String> buildFilePath;

    // SET by the collection creation process
    private String buildStatus;
    private int executables;
    private int libraries;
    private int archives;
    private List<String> errorMessage;
    private List<String> packageDependencies;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public boolean isHasDownloads() {
        return hasDownloads;
    }

    public void setHasDownloads(boolean hasDownloads) {
        this.hasDownloads = hasDownloads;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Date getPushedAt() {
        return pushedAt;
    }

    public void setPushedAt(Date pushedAt) {
        this.pushedAt = pushedAt;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getDefaultBranch() {
        return defaultBranch;
    }

    public void setDefaultBranch(String defaultBranch) {
        this.defaultBranch = defaultBranch;
    }

    public String getLatestCommitId() {
        return latestCommitId;
    }

    public void setLatestCommitId(String latestCommitId) {
        this.latestCommitId = latestCommitId;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public int getForksCount() {
        return forksCount;
    }

    public void setForksCount(int forksCount) {
        this.forksCount = forksCount;
    }

    public int getOpenIssuesCount() {
        return openIssuesCount;
    }

    public void setOpenIssuesCount(int openIssuesCount) {
        this.openIssuesCount = openIssuesCount;
    }

    public int getStargazersCount() {
        return stargazersCount;
    }

    public void setStargazersCount(int stargazersCount) {
        this.stargazersCount = stargazersCount;
    }

    public String getHtmlUrl() {
        return htmlUrl;
    }

    public void setHtmlUrl(String htmlUrl) {
        this.htmlUrl = htmlUrl;
    }

    public String getCloneUrl() {
        return cloneUrl;
    }

    public void setCloneUrl(String cloneUrl) {
        this.cloneUrl = cloneUrl;
    }

    public String getBuildSystem() {
        return buildSystem;
    }

    public void setBuildSystem(String buildSystem) {
        this.buildSystem = buildSystem;
    }

    public List<String> getBuildFilePath() {
        return buildFilePath;
    }

    public void setBuildFilePath(List<String> buildFilePath) {
        this.buildFilePath = buildFilePath;
    }

    public String getBuildStatus() {
        return buildStatus;
    }

    public void setBuildStatus(String buildStatus) {
        this.buildStatus = buildStatus;
    }

    public int getExecutables() {
        return executables;
    }

    public void setExecutables(int executables) {
        this.executables = executables;
    }

    public int getLibraries() {
        return libraries;
    }

    public void setLibraries(int libraries) {
        this.libraries = libraries;
    }

    public int getArchives() {
        return archives;
    }

    public void setArchives(int archives) {
        this.archives = archives;
    }

    public List<String> getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(List<String> errorMessages) {
        this.errorMessage = errorMessages;
    }

    public List<String> getPackageDependencies() {
        return packageDependencies;
    }

    public void setPackageDependencies(List<String> packageDependencies) {
        this.packageDependencies = packageDependencies;
    }
}
