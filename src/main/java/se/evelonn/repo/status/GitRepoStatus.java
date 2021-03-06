package se.evelonn.repo.status;

import java.util.HashSet;
import java.util.Set;

public class GitRepoStatus {

	private String repoName;

	private String branchName;

	private String remoteBranch;

	private Set<String> addedFiles = new HashSet<String>();

	private Set<String> changedFiles = new HashSet<String>();

	private Set<String> removedFiles = new HashSet<String>();

	private Set<String> untrackedFiles = new HashSet<String>();

	private Set<String> modifiedFiles = new HashSet<String>();

	private Set<String> missingFiles = new HashSet<String>();

	private int ahead;

	private int behind;

	private GitRepoStatus(GitRepoStatusBuilder builder) {
		this.repoName = builder.repoName;
		this.branchName = builder.branchName;
		this.remoteBranch = builder.remoteBranch;
		this.addedFiles = builder.addedFiles;
		this.changedFiles = builder.changedFiles;
		this.removedFiles = builder.removedFiles;
		this.untrackedFiles = builder.untrackedFiles;
		this.modifiedFiles = builder.modifiedFiles;
		this.missingFiles = builder.missingFiles;
		this.ahead = builder.ahead;
		this.behind = builder.behind;
	}

	public static RepoName builder() {
		return new GitRepoStatusBuilder();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("%-40s %s", Color.BLUE_FG.colorize(repoName), Color.GREEN_FG.colorize(branchName)));
		if (remoteBranch != null && !remoteBranch.isEmpty()) {
			sb.append(Color.GREEN_FG.colorize(" --> " + remoteBranch));
			if (ahead > 0) {
				sb.append(Color.YELLOW_FG.colorize(" ( ahead " + ahead + " )"));
			} else if (behind > 0) {
				sb.append(Color.YELLOW_FG.colorize(" ( behind " + behind + " )"));
			}
		}
		modifiedFiles.stream()
				.forEach(modified -> sb.append(Color.CYAN_FG.colorize("\nM  ") + Color.RED_FG.colorize(modified)));
		untrackedFiles.stream().forEach(
				untracked -> sb.append(Color.MAGENTA_FG.colorize("\n??  ") + Color.RED_FG.colorize(untracked)));
		addedFiles.stream().forEach(added -> sb.append("\nA  " + Color.RED_FG.colorize(added)));
		changedFiles.stream().forEach(changed -> sb.append("\nC  " + Color.RED_FG.colorize(changed)));
		removedFiles.stream().forEach(removed -> sb.append("\nR  " + Color.RED_FG.colorize(removed)));
		missingFiles.stream().forEach(missing -> sb.append("\n-  " + Color.RED_FG.colorize(missing)));
		return sb.toString();
	}

	private static class GitRepoStatusBuilder implements RepoName, BranchName, RemoteBranch, Ahead, Behind, Build {

		private String repoName;

		private String branchName;

		private String remoteBranch;

		private Set<String> addedFiles = new HashSet<String>();

		private Set<String> changedFiles = new HashSet<String>();

		private Set<String> removedFiles = new HashSet<String>();

		private Set<String> untrackedFiles = new HashSet<String>();

		private Set<String> modifiedFiles = new HashSet<String>();

		private Set<String> missingFiles = new HashSet<String>();

		private int behind;

		private int ahead;

		@Override
		public Build addedFiles(Set<String> addedFiles) {
			this.addedFiles = addedFiles;
			return this;
		}

		@Override
		public Build changedFiles(Set<String> changedFiles) {
			this.changedFiles = changedFiles;
			return this;
		}

		@Override
		public Build removedFiles(Set<String> removedFiles) {
			this.removedFiles = removedFiles;
			return this;
		}

		@Override
		public Build untrackedFiles(Set<String> untrackedFiles) {
			this.untrackedFiles = untrackedFiles;
			return this;
		}

		@Override
		public Build modifiedFiles(Set<String> modifiedFiles) {
			this.modifiedFiles = modifiedFiles;
			return this;
		}

		@Override
		public Build missingFiles(Set<String> missingFiles) {
			this.missingFiles = missingFiles;
			return this;
		}

		@Override
		public GitRepoStatus build() {
			return new GitRepoStatus(this);
		}

		@Override
		public Ahead remoteBranch(String stateDescription) {
			this.remoteBranch = stateDescription;
			return this;
		}

		@Override
		public RemoteBranch branchName(String branchName) {
			this.branchName = branchName;
			return this;
		}

		@Override
		public BranchName repoName(String repoName) {
			this.repoName = repoName;
			return this;
		}

		@Override
		public Build behind(int behind) {
			this.behind = behind;
			return this;
		}

		@Override
		public Behind ahead(int ahead) {
			this.ahead = ahead;
			return this;
		}
	}

	public interface RepoName {
		BranchName repoName(String repoName);
	}

	public interface BranchName {
		RemoteBranch branchName(String branchName);
	}

	public interface RemoteBranch {
		Ahead remoteBranch(String remoteBranch);
	}

	public interface Ahead {
		Behind ahead(int ahead);
	}

	public interface Behind {
		Build behind(int behind);
	}

	public interface Build {
		Build addedFiles(Set<String> addedFiles);

		Build changedFiles(Set<String> changedFiles);

		Build removedFiles(Set<String> removedFiles);

		Build untrackedFiles(Set<String> untrackedFiles);

		Build modifiedFiles(Set<String> modifiedFiles);

		Build missingFiles(Set<String> missingFiles);

		GitRepoStatus build();
	}
}