package mc.obliviate.singledungeon.match.state;

import mc.obliviate.singledungeon.match.DungeonMatch;

public interface ArenaState {

    void next();

    DungeonMatch getDungeonArena();

}
