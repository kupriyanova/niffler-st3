package guru.qa.niffler.jupiter.user;

import guru.qa.niffler.model.FriendState;
import guru.qa.niffler.model.UserJson;
import io.qameta.allure.AllureId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.*;

import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static guru.qa.niffler.model.FriendState.*;

public class UserQueueExtension implements BeforeEachCallback, AfterTestExecutionCallback, ParameterResolver {

  public static ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(UserQueueExtension.class);
  private static Map<User.UserType, Queue<UserJson>> usersQueue = new ConcurrentHashMap<>();

  static {
    Queue<UserJson> usersWithFriends = new ConcurrentLinkedQueue<>();
    usersWithFriends.add(bindUser("dima", "12345", FRIEND, List.of("barsik")));
    usersWithFriends.add(bindUser("barsik", "12345", FRIEND, List.of("dima")));
    usersQueue.put(User.UserType.WITH_FRIENDS, usersWithFriends);
    Queue<UserJson> usersInSent = new ConcurrentLinkedQueue<>();
    usersInSent.add(bindUser("bee", "12345", INVITE_SENT, List.of("pizzly")));
    usersInSent.add(bindUser("anna", "12345", INVITE_SENT, List.of("valentin")));
    usersQueue.put(User.UserType.INVITATION_SENT, usersInSent);
    Queue<UserJson> usersInRc = new ConcurrentLinkedQueue<>();
    usersInRc.add(bindUser("valentin", "12345", INVITE_RECEIVED, List.of("anna")));
    usersInRc.add(bindUser("pizzly", "12345", INVITE_RECEIVED, List.of("bee")));
    usersQueue.put(User.UserType.INVITATION_RECEIVED, usersInRc);
  }

  @Override
  public void beforeEach(ExtensionContext context) {
    // фильтруем методы с аннотацией
    List<Method> methods = Arrays.stream(context.getRequiredTestClass().getDeclaredMethods())
        .filter(method -> method.isAnnotationPresent(BeforeEach.class) || method.isAnnotationPresent(Test.class))
        .toList();

    // собираем все параметры из всех методов
    List<Parameter> parameters = new ArrayList<>();
    for(Method method : methods) {
      Parameter[] p = method.getParameters();
      if (p != null) parameters.addAll(List.of(p));
    }
//    Parameter[] parameters = context.getRequiredTestMethod().getParameters();

    Map<User.UserType, UserJson> usersForTest = new HashMap<>(parameters.size());
    for (Parameter parameter : parameters) {
      if (parameter.getType().isAssignableFrom(UserJson.class)) {
        User parameterAnnotation = parameter.getAnnotation(User.class);
        User.UserType userType = parameterAnnotation.userType();
        Queue<UserJson> usersQueueByType = usersQueue.get(parameterAnnotation.userType());
        UserJson candidateForTest = null;
        while (candidateForTest == null) {
          candidateForTest = usersQueueByType.poll();
        }
        candidateForTest.setUserType(userType);
        usersForTest.put(userType, candidateForTest);
        context.getStore(NAMESPACE).put(getAllureId(context), usersForTest);
      }
    }
  }

  @Override
  public void afterTestExecution(ExtensionContext context) {
    Map<User.UserType, UserJson> usersFromTest = context.getStore(NAMESPACE).get(getAllureId(context), Map.class);
    for (User.UserType userType : usersFromTest.keySet()) usersQueue.get(userType).add(usersFromTest.get(userType));
  }

  @Override
  public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    return parameterContext.getParameter().getType().isAssignableFrom(UserJson.class)
        && parameterContext.getParameter().isAnnotationPresent(User.class);
  }

  @Override
  public UserJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    User.UserType userType = parameterContext.getParameter().getAnnotation(User.class).userType();
    return (UserJson) extensionContext.getStore(NAMESPACE).get(getAllureId(extensionContext), Map.class).get(userType);
  }


  private String getAllureId(ExtensionContext context) {
    AllureId allureId = context.getRequiredTestMethod().getAnnotation(AllureId.class);
    if (allureId == null) {
      throw new IllegalStateException("Annotation @AllureId must be present!");
    }
    return allureId.value();
  }

  private static UserJson bindUser(String username, String password, FriendState friendState, List<String> friendsUserName) {
    UserJson user = new UserJson()
        .setUsername(username)
        .setPassword(password)
        .setFriendState(friendState)
        .setFriendsUserName(friendsUserName);
    return user;
  }
}
