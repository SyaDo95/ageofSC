package server;

import java.io.Serializable;

public class GameAction implements Serializable {
    private String actionType; // SPAWN, MOVE, ATTACK 등
    private UnitState unitState; // 관련된 유닛 상태
    private String playerId;    // 명령을 보낸 플레이어

    public GameAction(String actionType, String playerId) {
        this.actionType = actionType;
        this.playerId = playerId;
        this.unitState = null; // unitState는 null로 설정
    }
    
    // 새로 추가된 생성자
    public GameAction(String actionType, UnitState unitState, String playerId) {
        this.actionType = actionType;
        this.unitState = unitState;
        this.playerId = playerId;
    }

    public String getActionType() { return actionType; }
    public UnitState getUnitState() { return unitState; }
    public String getPlayerId() { return playerId; }

    @Override
    public String toString() {
        return "GameAction{" +
                "actionType='" + actionType + '\'' +
                ", unitState=" + unitState +
                ", playerId='" + playerId + '\'' +
                '}';
    }
}
