package net.onlinelibrary.dto.view;

public final class Views {
    public interface ForEvery {}

    public interface ForUser extends ForEvery {}

    public interface ForModerator extends ForUser {}

    public interface ForAdmin extends ForModerator {}

    public interface ForSuperAdmin extends ForAdmin {}
}
