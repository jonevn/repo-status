package se.evelonn.repo.status;

import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Function;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.StatusCommand;

public class RepoStatus {

	public static void printRepoStatus(String directory) throws Exception {

		Path baseDirectory = Paths.get(directory);

		if (!baseDirectory.toFile().isDirectory()) {
			throw new IllegalArgumentException(directory + " is not a directory");
		}

		DirectoryStream<Path> directoryStream = Files.newDirectoryStream(baseDirectory);
		directoryStream.forEach(path -> {
			if (path.toFile().isDirectory() && path.resolve(".git").toFile().exists()) {
				System.out.println(toGitRepoStatus);
			}
		});
	}

	private static Function<Path, GitRepoStatus> toGitRepoStatus = (path) -> {
		try {
			Git git = Git.open(path.toFile());
			// Performing fetch
			git.fetch().call();

			git.getRepository().getRepositoryState();
			StatusCommand status = git.status();
			Status statusCall = status.call();

			return GitRepoStatus.builder() //
					.repoName("") //
					.branchName(git.getRepository().getFullBranch()) //
					.stateDescription(git.getRepository().getRepositoryState().getDescription()) //
					.build();

		} catch (Exception e) {
			throw new RuntimeException("Failed to check status for git-repo (" + path.toFile().getAbsolutePath() + ")"
					+ ": " + e.getMessage(), e);
		}
	};
}
