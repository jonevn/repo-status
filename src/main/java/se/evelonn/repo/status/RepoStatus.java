package se.evelonn.repo.status;

import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Function;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.StatusCommand;
import org.eclipse.jgit.lib.BranchTrackingStatus;

public class RepoStatus {

	public static void printRepoStatus(String directory) throws Exception {

		Path baseDirectory = Paths.get(directory);

		if (!baseDirectory.toFile().isDirectory()) {
			throw new IllegalArgumentException(directory + " is not a directory");
		}

		DirectoryStream<Path> directoryStream = Files.newDirectoryStream(baseDirectory);
		directoryStream.forEach(path -> {
			if (path.toFile().isDirectory() && path.resolve(".git").toFile().exists()) {
				System.out.println(toGitRepoStatus.apply(path));
			}
		});
	}

	private static Function<Path, GitRepoStatus> toGitRepoStatus = (path) -> {
		try {
			Git git = Git.open(path.toFile());

			StatusCommand status = git.status();
			Status statusCall = status.call();

			BranchTrackingStatus branchTrackingStatus = BranchTrackingStatus.of(git.getRepository(),
					git.getRepository().getBranch());

			return GitRepoStatus.builder() //
					.repoName(path.toFile().getName()) //
					.branchName(git.getRepository().getBranch()) //
					.remoteBranch(branchTrackingStatus.getRemoteTrackingBranch().replaceAll("refs/remotes/", "")) //
					.ahead(branchTrackingStatus.getAheadCount()) //
					.behind(branchTrackingStatus.getBehindCount()) //
					.addedFiles(statusCall.getAdded()) //
					.changedFiles(statusCall.getChanged()) //
					.missingFiles(statusCall.getMissing()) //
					.modifiedFiles(statusCall.getModified()) //
					.removedFiles(statusCall.getRemoved()) //
					.untrackedFiles(statusCall.getUntracked()) //
					.build();

		} catch (Exception e) {
			throw new RuntimeException("Failed to check status for git-repo (" + path.toFile().getAbsolutePath() + ")"
					+ ": " + e.getMessage(), e);
		}
	};
}