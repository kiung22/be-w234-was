package repository;

public class RepositoryContainer {

    private RepositoryContainer() {}

    private static final UserRepository userRepository = new UserRepository();
    private static final MemoRepository memoRepository = new MemoRepository();

    public static UserRepository getUserRepository() {
        return userRepository;
    }

    public static MemoRepository getMemoRepository() {
        return memoRepository;
    }
}
