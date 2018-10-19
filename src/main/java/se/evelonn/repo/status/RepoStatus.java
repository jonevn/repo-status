package se.evelonn.repo.status;

import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
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

		List<Path> directories = new ArrayList<>();
		directoryStream.forEach(path -> {
			if (path.toFile().isDirectory() && path.resolve(".git").toFile().exists()) {
				directories.add(path);

			}
		});

		directories.stream().sorted(Comparator.naturalOrder()).forEach(path -> System.out.println(toGitRepoStatus.apply(path)));

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
					.remoteBranch(branchTrackingStatus == null ? "" : branchTrackingStatus.getRemoteTrackingBranch().replaceAll("refs/remotes/", "")) //
					.ahead(branchTrackingStatus == null ? 0 : branchTrackingStatus.getAheadCount()) //
					.behind(branchTrackingStatus == null ? 0 : branchTrackingStatus.getBehindCount()) //
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