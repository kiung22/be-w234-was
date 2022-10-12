package service;

import repository.RepositoryContainer;

public class ServiceContainer {

    private ServiceContainer() {}

    private static final UserService userService = new UserService(RepositoryContainer.getUserRepository());
    private static final MemoService memoService = new MemoService(RepositoryContainer.getMemoRepository());

    public static UserService getUserService() {
        return userService;
    }

    public static MemoService getMemoService() {
        return memoService;
    }
}
