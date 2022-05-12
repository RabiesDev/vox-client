package dev.rabies.vox.friend;

import lombok.Getter;
import lombok.Setter;

public class Friend {

    @Getter @Setter
    private String ign, nick;

    public Friend(String ign) {
        this(ign, ign);
    }

    public Friend(String ign, String nick) {
        this.ign = ign;
        this.nick = nick;
    }
}
