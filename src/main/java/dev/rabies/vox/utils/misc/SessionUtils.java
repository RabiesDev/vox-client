package dev.rabies.vox.utils.misc;

import com.mojang.authlib.Agent;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import com.thealtening.auth.TheAlteningAuthentication;
import com.thealtening.auth.service.AlteningServiceType;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;

import java.net.Proxy;

public class SessionUtils {

    private final Minecraft mc = Minecraft.getMinecraft();
    private final TheAlteningAuthentication theAlteningAuth = TheAlteningAuthentication.theAltening();
    private final YggdrasilUserAuthentication authentication;

    public SessionUtils() {
        authentication = (YggdrasilUserAuthentication) new YggdrasilAuthenticationService(
                Proxy.NO_PROXY,
                ""
        ).createUserAuthentication(Agent.MINECRAFT);
    }

    public static SessionUtils newSession() {
        return new SessionUtils();
    }

    public SessionUtils theAltening(String var0) {
        theAlteningAuth.updateService(AlteningServiceType.THEALTENING);
        authentication.setUsername(var0);
        authentication.setPassword("1337");
        return this;
    }

    public SessionUtils username(String username) {
        authentication.setUsername(username);
        return this;
    }

    public SessionUtils password(String password) {
        theAlteningAuth.updateService(AlteningServiceType.MOJANG);
        authentication.setPassword(password);
        return this;
    }

    public boolean login() {
        try {
            authentication.logIn();
            GameProfile profile = authentication.getSelectedProfile();
            if (profile == null) return false;
            mc.session = new Session(profile.getName(), profile.getId().toString(),
                    authentication.getAuthenticatedToken(), "MOJANG");
            return true;
        } catch (AuthenticationException e) {
            e.printStackTrace();
            return false;
        }
    }
}
