public class SubTask extends Task {
    private int epicId;

    public SubTask(String name, String description) {
        super(name, description);

    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int id) {
        epicId = id;
    }

    @Override
    public String toString() {
        return "id = " + getId() + ", epicId = " + getEpicId() +
                ", Название = " + getName() +
                ", Описание = " + getDescription() +
                ", Статус = " + getStatus();
    }
}
