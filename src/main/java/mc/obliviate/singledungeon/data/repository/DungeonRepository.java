package mc.obliviate.singledungeon.data.repository;

import mc.obliviate.singledungeon.stats.DungeonStatistics;
import org.bukkit.Bukkit;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class DungeonRepository extends Repository {

    public DungeonRepository() {
        super("singledungeon");
        super.annotatedClasses(DungeonStatistics.class);
        super.connect();
    }

    public void save(DungeonStatistics statistics) {
        super.session.getTransaction().begin();
        super.session.persist(statistics);
        super.session.getTransaction().commit();
    }

    public void delete(DungeonStatistics statistics) {
        super.session.getTransaction().begin();
        super.session.remove(statistics);
        super.session.getTransaction().commit();
    }

    public Optional<DungeonStatistics> findByUID(UUID uid) {
        return Optional.ofNullable(super.session.get(DungeonStatistics.class, uid));
    }

    public DungeonStatistics getByUID(UUID uid) {
        return this.findByUID(uid).orElseThrow(() -> new RuntimeException("statistics not found!"));
    }

    public List<DungeonStatistics> findAll() {
        return super.session.createQuery("SELECT a FROM Statistics a", DungeonStatistics.class).getResultList();
    }

}
