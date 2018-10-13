package se.evelonn.repo.status;

import java.util.ArrayList;
import java.util.List;

public class GitRepoStatus {

	private String repoName;

	private String branchName;

	private String stateDescription;

	private List<String> addedFiles = new ArrayList<>();

	private List<String> changedFiles = new ArrayList<>();

	private List<String> removedFiles = new ArrayList<>();

	private List<String> untrackedFiles = new ArrayList<>();

	private List<String> modifiedFiles = new ArrayList<>();

	private List<String> missingFiles = new ArrayList<>();

	private GitRepoStatus(GitRepoStatusBuilder builder) {
		this.repoName = builder.repoName;
	}

	public static RepoName builder() {
		return new GitRepoStatusBuilder();
	}

	private static class GitRepoStatusBuilder implements RepoName, BranchName, StateDescription, Build {

		private String repoName;

		private String branchName;

		private String stateDescription;

		private List<String> addedFiles = new ArrayList<>();

		private List<String> changedFiles = new ArrayList<>();

		private List<String> removedFiles = new ArrayList<>();

		private List<String> untrackedFiles = new ArrayList<>();

		private List<String> modifiedFiles = new ArrayList<>();

		private List<String> missingFiles = new ArrayList<>();

		@Override
		public Build addedFiles(List<String> addedFiles) {
			this.addedFiles = addedFiles;
			return this;
		}

		@Override
		public Build changedFiles(List<String> changedFiles) {
			this.changedFiles = changedFiles;
			return this;
		}

		@Override
		public Build removedFiles(List<String> removedFiles) {
			this.removedFiles = removedFiles;
			return this;
		}

		@Override
		public Build untrackedFiles(List<String> untrackedFiles) {
			this.untrackedFiles = untrackedFiles;
			return this;
		}

		@Override
		public Build modifiedFiles(List<String> modifiedFiles) {
			this.modifiedFiles = modifiedFiles;
			return this;
		}

		@Override
		public Build missingFiles(List<String> missingFiles) {
			this.missingFiles = missingFiles;
			return this;
		}

		@Override
		public GitRepoStatus build() {
			return new GitRepoStatus(this);
		}

		@Override
		public Build stateDescription(String stateDescription) {
			this.stateDescription = stateDescription;
			return this;
		}

		@Override
		public StateDescription branchName(String branchName) {
			this.branchName = branchName;
			return this;
		}

		@Override
		public BranchName repoName(String repoName) {
			this.repoName = repoName;
			return this;
		}
	}

	public interface RepoName {
		BranchName repoName(String repoName);
	}

	public interface BranchName {
		StateDescription branchName(String branchName);
	}

	public interface StateDescription {
		Build stateDescription(String stateDescription);
	}

	public interface Build {
		Build addedFiles(List<String> addedFiles);

		Build changedFiles(List<String> changedFiles);

		Build removedFiles(List<String> removedFiles);

		Build untrackedFiles(List<String> untrackedFiles);

		Build modifiedFiles(List<String> modifiedFiles);

		Build missingFiles(List<String> missingFiles);

		GitRepoStatus build();
	}
}