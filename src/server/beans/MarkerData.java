package server.beans;

/**
 * MarkerData,
 * Holds the markerId (1-9) depending on which tile it is, the markerType depending on if it is a X or O
 * and the opponents username for knowing who to end the data to.
 */

/**
 * Created by Jonas on 2016-05-26.
 */

public class MarkerData {
    private int markerId;
    private String markerType;
    private String opponentUsername;

    public MarkerData(int markerId, String markerType, String opponentUsername) {
        this.markerId = markerId;
        this.markerType = markerType;
        this.opponentUsername = opponentUsername;
    }

    public int getMarkerId() {
        return markerId;
    }

    public void setMarkerId(int markerId) {
        this.markerId = markerId;
    }

    public String getMarkerType() {
        return markerType;
    }

    public void setMarkerType(String markerType) {
        this.markerType = markerType;
    }

    public String getOpponentUsername() {
        return opponentUsername;
    }

    public void setOpponentUsername(String opponentUsername) {
        this.opponentUsername = opponentUsername;
    }
}
