package domain;

public enum WorkerEventType {
    MENU("""
                    Options:
                    :q - exit room/chat
                    :room number - create/join room at this number, e.g. :room 1
                    :upload file_path - upload a file
                    :download - download a file
                    :help - display this menu
                    """
    ),
    QUIT(":q"),
    JOIN(":room"),
    UPLOAD(":upload"),
    DOWNLOAD(":download"),
    HELP(":help"),
    ERROR("Error during file upload/download");
    public final String label;

    private WorkerEventType(String label){
        this.label = label;
    }


}
