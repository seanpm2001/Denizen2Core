package org.mcmonkey.denizen2core;

import org.mcmonkey.denizen2core.commands.CommandEntry;
import org.mcmonkey.denizen2core.commands.CommandQueue;

/**
 * Abstract class representing an implementation of Denizen2.
 */
public abstract class Denizen2Implementation {

    public abstract void outputException(Exception ex);

    public abstract void outputInfo(String text);

    public abstract void outputInvalid(CommandQueue queue, CommandEntry entry);

    public abstract void outputError(String message);
}
