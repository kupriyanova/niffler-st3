package guru.qa.niffler.jupiter.dbUser;

import guru.qa.niffler.db.dao.AuthUserDAO;
import guru.qa.niffler.db.dao.AuthUserDAOJdbc;
import guru.qa.niffler.db.dao.UserDataUserDAO;
import guru.qa.niffler.db.model.AuthUserEntity;
import guru.qa.niffler.db.model.Authority;
import guru.qa.niffler.db.model.AuthorityEntity;
import guru.qa.niffler.jupiter.DaoExtension;
import guru.qa.niffler.jupiter.ExtensionsHelper;
import org.junit.jupiter.api.extension.*;

import java.util.Arrays;
@ExtendWith(DaoExtension.class)
class DbUserExtension implements BeforeEachCallback, ParameterResolver, AfterTestExecutionCallback {

    public ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(DbUserExtension.class);

    private final AuthUserDAO authUser = new AuthUserDAOJdbc();
    private final UserDataUserDAO udUser = new AuthUserDAOJdbc();

    @Override
    public void beforeEach(ExtensionContext context) {
        DBUser annotation = context.getRequiredTestMethod().getAnnotation(DBUser.class);
        if (annotation != null) {
            AuthUserEntity createdUser = new AuthUserEntity()
                .setUsername(annotation.username())
                .setPassword(annotation.password())
                .setEnabled(true)
                .setAccountNonExpired(true)
                .setAccountNonLocked(true)
                .setCredentialsNonExpired(true)
                .setAuthorities(Arrays.stream(Authority.values())
                    .map(a -> {
                        AuthorityEntity ae = new AuthorityEntity();
                        ae.setAuthority(a);
                        return ae;
                    }).toList());

//            context.getStore(NAMESPACE).put("createdUser", createdUser);
            context.getStore(NAMESPACE).put(
                "createdUser"+ new ExtensionsHelper().getAllureId(context),
                createdUser);
        }
    }

    @Override
    public void afterTestExecution(ExtensionContext context) {
        AuthUserEntity createdUser = context.getStore(NAMESPACE)
            .get("createdUser"+ new ExtensionsHelper().getAllureId(context), AuthUserEntity.class);
        udUser.deleteUserInUserData(createdUser.getUsername());
        authUser.deleteUser(createdUser.getId());
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
        throws ParameterResolutionException {
        return parameterContext.getParameter()
            .getType()
            .isAssignableFrom(AuthUserEntity.class);
    }

    @Override
    public AuthUserEntity resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
        throws ParameterResolutionException {
        return extensionContext
            .getStore(NAMESPACE)
            .get("createdUser"+ new ExtensionsHelper().getAllureId(extensionContext), AuthUserEntity.class);
    }
}
