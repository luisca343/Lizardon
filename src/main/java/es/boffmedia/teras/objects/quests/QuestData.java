package es.boffmedia.teras.objects.quests;

import noppes.npcs.api.handler.data.IQuest;

public class QuestData {
    private int id;
    private String name;
    private String logText;
    private String completeText;
    private boolean repeatable;
    private int type;
    private int nextQuest;
    private String category;
    private QuestStatus status;

    public QuestData(IQuest quest){
        this.id = quest.getId();
        this.name = quest.getName();
        this.logText = quest.getLogText();
        this.completeText = quest.getCompleteText();
        this.repeatable = quest.getIsRepeatable();
        this.type = quest.getType();
        this.nextQuest = quest.getNextQuest() != null ? quest.getNextQuest().getId() : -1;
        this.category = quest.getCategory().getName();
        this.status = QuestStatus.NOT_STARTED;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogText() {
        return logText;
    }

    public void setLogText(String logText) {
        this.logText = logText;
    }

    public String getCompleteText() {
        return completeText;
    }

    public void setCompleteText(String completeText) {
        this.completeText = completeText;
    }

    public boolean isRepeatable() {
        return repeatable;
    }

    public void setRepeatable(boolean repeatable) {
        this.repeatable = repeatable;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getNextQuest() {
        return nextQuest;
    }

    public void setNextQuest(int nextQuest) {
        this.nextQuest = nextQuest;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public QuestStatus getStatus() {
        return status;
    }

    public void setStatus(QuestStatus status) {
        this.status = status;
    }
}
