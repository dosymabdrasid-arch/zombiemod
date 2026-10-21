package kz.zombiemod;

/** Типы заражённых. Все параметры в одном месте — легко настраивать баланс. */
public enum Variant {
    //        id       hp     speed  dmg   kbRes scale poison weight min max
    RUNNER   ("runner",   14, 0.36, 3.0, 0.0, 0.95f, false, 10, 1, 3),
    TOXIC    ("toxic",    24, 0.24, 3.0, 0.1, 1.00f, true,   6, 1, 2),
    BRUTE    ("brute",    60, 0.20, 8.0, 0.6, 1.30f, false,  3, 1, 1),
    TYRANT   ("tyrant",  200, 0.27, 16.0, 1.0, 1.90f, false, 1, 1, 1);

    public final String id;
    public final double health, speed, damage, kbResist;
    public final float scale;
    public final boolean poison;
    public final int weight, minGroup, maxGroup;

    Variant(String id, double health, double speed, double damage, double kbResist,
            float scale, boolean poison, int weight, int minGroup, int maxGroup) {
        this.id = id; this.health = health; this.speed = speed; this.damage = damage;
        this.kbResist = kbResist; this.scale = scale; this.poison = poison;
        this.weight = weight; this.minGroup = minGroup; this.maxGroup = maxGroup;
    }
}
