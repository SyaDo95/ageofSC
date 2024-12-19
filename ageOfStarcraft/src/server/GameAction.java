package server;

//--- 직렬화 객체 ---

import java.io.Serializable;

public class GameAction implements Serializable {
 private static final long serialVersionUID = 1L;

 private String actionType; // e.g., "spawn_unit", "update_unit"
 private String unitType;  // e.g., "SCV", "Marine"
 private String team;      // e.g., "LEFT", "RIGHT"
 private int x;
 private int y;

 public GameAction(String actionType, String unitType, String team, int x, int y) {
     this.actionType = actionType;
     this.unitType = unitType;
     this.team = team;
     this.x = x;
     this.y = y;
 }

 public String getActionType() {
     return actionType;
 }

 public String getUnitType() {
     return unitType;
 }

 public String getTeam() {
     return team;
 }

 public int getX() {
     return x;
 }

 public int getY() {
     return y;
 }
}
