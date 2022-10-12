package controller;

import service.ServiceContainer;

public class ControllerContainer {

    private ControllerContainer() {}

    private static final IndexController indexController = new IndexController(ServiceContainer.getMemoService());
    private static final LoginController loginController = new LoginController(ServiceContainer.getUserService());
    private static final MemoController memoController = new MemoController(ServiceContainer.getMemoService(), ServiceContainer.getUserService());
    private static final SignupController signupController = new SignupController(ServiceContainer.getUserService());
    private static final StaticFileController staticFileController = new StaticFileController();
    private static final UserListController userListController = new UserListController(ServiceContainer.getUserService());

    public static IndexController getIndexController() {
        return indexController;
    }

    public static LoginController getLoginController() {
        return loginController;
    }

    public static MemoController getMemoController() {
        return memoController;
    }

    public static SignupController getSignupController() {
        return signupController;
    }

    public static StaticFileController getStaticFileController() {
        return staticFileController;
    }

    public static UserListController getUserListController() {
        return userListController;
    }
}
