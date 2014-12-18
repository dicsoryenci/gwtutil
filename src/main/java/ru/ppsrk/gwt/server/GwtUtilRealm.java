package ru.ppsrk.gwt.server;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;

import ru.ppsrk.gwt.client.ClientAuthException;
import ru.ppsrk.gwt.client.ClientAuthenticationException;
import ru.ppsrk.gwt.client.ClientAuthorizationException;
import ru.ppsrk.gwt.client.LogicException;
import ru.ppsrk.gwt.dto.UserDTO;

public abstract class GwtUtilRealm extends AuthorizingRealm {

    public class HashedBase64Password {
        String password;
        String salt;

        public HashedBase64Password(String password, RandomNumberGenerator rng) {
            ByteSource salt = rng.nextBytes();
            HashedCredentialsMatcher matcher = ((HashedCredentialsMatcher) GwtUtilRealm.this.getCredentialsMatcher());
            this.password = new SimpleHash(matcher.getHashAlgorithmName(), password, salt, matcher.getHashIterations()).toBase64();
            this.salt = salt.toBase64();
        }
    }

    public boolean login(final String username, String password, boolean remember) throws LogicException, ClientAuthException {
        Subject subject = SecurityUtils.getSubject();
        try {
            subject.login(new UsernamePasswordToken(username, password, remember));
            if (subject.isAuthenticated()) {
                loginSuccessful(username);
            }
        } catch (AuthenticationException e) {
            throw new ClientAuthenticationException(e.getMessage());
        } catch (AuthorizationException e) {
            throw new ClientAuthorizationException(e.getMessage());
        }
        return subject.isAuthenticated();
    }

    public abstract Long register(final String username, final String password, RandomNumberGenerator rng) throws LogicException,
            ClientAuthException;

    public abstract List<String> getRoles(String principal) throws LogicException, ClientAuthException;

    public abstract List<String> getPerms(String principal) throws LogicException, ClientAuthException;

    public abstract UserDTO getUser(String principal) throws LogicException, ClientAuthException;

    public void loginSuccessful(String username) throws LogicException, ClientAuthException {

    }
}
