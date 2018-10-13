package se.evelonn.repo.status;

public class Main {

	public static void main(String[] args) throws Exception {

		if (args.length != 1) {
			System.err.println("Program needs one argument to run");
			System.exit(1);
		}

		RepoStatus.printRepoStatus(args[0]);
	}
}