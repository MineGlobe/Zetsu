package me.blazingtide.zetsu.permissible;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;

/**
 * Basically conditions / permissions. Ran before the command is invoked
 * to test weather the command should be invoked or not.
 * <p>
 * ANNOTATION BASED
 *
 * @param <T>
 */
public interface PermissibleAttachment<T extends Annotation> {

    boolean test(T annotation, @NotNull CommandSender sender);

    void onFail(@NotNull CommandSender sender, T annotation);

}
