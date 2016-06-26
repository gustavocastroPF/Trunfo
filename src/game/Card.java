package game;

import java.util.ArrayList;

/**
 *
 * @author gustavo
 */
public class Card {

    private String name;
    private Integer id;
    private Integer attack;
    private Integer defense;
    private Integer ability;
    private Session player;

    public Card() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAttack() {
        return attack;
    }

    public void setAttack(Integer attack) {
        this.attack = attack;
    }

    public Integer getDefense() {
        return defense;
    }

    public void setDefense(Integer defense) {
        this.defense = defense;
    }

    public Integer getAbility() {
        return ability;
    }

    public void setAbility(Integer ability) {
        this.ability = ability;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Session getPlayer() {
        return player;
    }

    public void setPlayer(Session player) {
        this.player = player;
    }

}
