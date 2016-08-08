package org.mcmonkey.denizen2core.commands.queuecommands;

import org.mcmonkey.denizen2core.commands.AbstractCommand;
import org.mcmonkey.denizen2core.commands.CommandEntry;
import org.mcmonkey.denizen2core.commands.CommandQueue;
import org.mcmonkey.denizen2core.tags.AbstractTagObject;
import org.mcmonkey.denizen2core.tags.objects.IntegerTag;
import org.mcmonkey.denizen2core.tags.objects.ListTag;
import org.mcmonkey.denizen2core.tags.objects.NullTag;
import org.mcmonkey.denizen2core.utilities.CoreUtilities;
import org.mcmonkey.denizen2core.utilities.debugging.ColorSet;

public class ForeachCommand extends AbstractCommand {

    public static class ForeachCommandData {
        public int current = 0;
        public ListTag list;
    }

    // <--[command]
    // @Name foreach
    // @Arguments 'stop'/'continue'/'start' [list]
    // @Short runs a block of code once for each entry in a list.
    // @Updated 2016/08/08
    // @Authors mcmonkey
    // @Group Queue
    // @Minimum 1
    // @Maximum 2
    // @Tag <def[foreach_index]> (IntegerTag) returns the current index in the loop.
    // @Tag <def[foreach_value]> (Dynamic) returns the current object in the loop.
    // @Tag <def[foreach_list]> (ListTag) returns the list of values in the loop.
    // @Description
    // Runs a block of code once for each entry in a list.
    // TODO: Explain more!
    // @Example
    // # This example echoes "hello" and then "world".
    // - foreach start hello|world:
    //   - echo "<def[foreach_value]>"
    // -->

    @Override
    public String getName() {
        return "foreach";
    }

    @Override
    public String getArguments() {
        return "'stop'/'continue'/'start' [list]";
    }

    @Override
    public int getMinimumArguments() {
        return 1;
    }

    @Override
    public int getMaximumArguments() {
        return 2;
    }

    @Override
    public boolean isWaitable() {
        return false;
    }

    @Override
    public void execute(CommandQueue queue, CommandEntry entry) {
        if (entry.arguments.get(0).toString().equals("\0CALLBACK")) {
            ForeachCommandData fcd = (ForeachCommandData) queue.commandStack.peek().entries[entry.blockStart - 1].getData(queue);
            fcd.current++;
            queue.commandStack.peek().setDefinition("foreach_index", new IntegerTag(fcd.current));
            queue.commandStack.peek().setDefinition("foreach_list", fcd.list);
            if (fcd.current <= fcd.list.getInternal().size()) {
                queue.commandStack.peek().setDefinition("foreach_value", fcd.list.getInternal().get(fcd.current - 1));
                if (queue.shouldShowGood()) {
                    queue.outGood("Foreach looping " + ColorSet.emphasis + fcd.current + "/" + fcd.list.getInternal().size());
                }
                queue.commandStack.peek().goTo(entry.blockStart);
            }
            else {
                queue.commandStack.peek().setDefinition("foreach_value", new NullTag());
                if (queue.shouldShowGood()) {
                    queue.outGood("Foreach completed!");
                }
            }
            return;
        }
        AbstractTagObject cobj = entry.getArgumentObject(queue, 0);
        String val = cobj.toString();
        if (val.equals("stop")) {
            // TODO: Impl!
        }
        else if (val.equals("next")) {
            // TODO: Impl!
        }
        else if (val.equals("start") && entry.arguments.size() > 1) {
            ListTag ltag = ListTag.getFor(queue.error, entry.getArgumentObject(queue, 1));
            if (ltag.getInternal().size() <= 0) {
                if (queue.shouldShowGood()) {
                    queue.outGood("Foreach count is 0, skipping.");
                }
                queue.commandStack.peek().goTo(entry.blockEnd + 1);
            }
            ListTag nltag = new ListTag();
            nltag.getInternal().addAll(ltag.getInternal());
            ForeachCommandData fcd = new ForeachCommandData();
            fcd.current = 1;
            fcd.list = nltag;
            entry.setData(queue, fcd);
            queue.commandStack.peek().setDefinition("foreach_index", new IntegerTag(fcd.current));
            queue.commandStack.peek().setDefinition("foreach_value", fcd.list.getInternal().get(fcd.current - 1));
            queue.commandStack.peek().setDefinition("foreach_list", fcd.list);
            if (queue.shouldShowGood()) {
                queue.outGood("Foreach count is is " + ColorSet.emphasis + fcd.list.getInternal().size() + ColorSet.good + ", repeating...");
            }
        }
        else {
            queue.handleError(entry, "Invalid arguments to foreach command!");
        }
    }
}
