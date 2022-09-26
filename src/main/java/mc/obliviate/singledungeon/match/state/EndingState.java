package mc.obliviate.singledungeon.match.state;

import mc.obliviate.singledungeon.match.DungeonMatch;
import org.bukkit.ChatColor;

public class EndingState implements ArenaState {
    private final DungeonMatch dungeonMatch;

    public EndingState(DungeonMatch dungeonMatch) {
        this.dungeonMatch = dungeonMatch;
        this.dungeonMatch.sendMessageToDungeonPlayers(ChatColor.GREEN + "Match ending in" + ChatColor.YELLOW + "3s");
        this.dungeonMatch.runLater(20, () -> this.dungeonMatch.sendMessageToDungeonPlayers(ChatColor.GREEN + "Match ending in" + ChatColor.YELLOW + " 2s"));
        this.dungeonMatch.runLater(40, () -> this.dungeonMatch.sendMessageToDungeonPlayers(ChatColor.GREEN + "Match ending in" + ChatColor.YELLOW + " 1s"));
        this.dungeonMatch.runLater(60, () -> {
            this.dungeonMatch.sendMessageToDungeonPlayers(ChatColor.GREEN + "Match ending...");
            next();
        });
    }

    @Override
    public void next() {
        this.dungeonMatch.uninstall();
    }

    @Override
    public DungeonMatch getDungeonArena() {
        return dungeonMatch;
    }

}
