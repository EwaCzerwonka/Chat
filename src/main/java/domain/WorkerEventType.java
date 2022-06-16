package domain;

public enum WorkerEventType {
    MENU("""
                    Options:
                    :q - exit room/chat
                    :room number - create/join room at this number, e.g. :room 1
                    :upload file_path - upload a file
                    :download - download a file
                    :log room_number - read history log from room / main room is 0 or nothing
                    :help - display this menu
                    """
    ),
    QUIT(":q"),
    JOIN(":room"),
    UPLOAD(":upload"),
    DOWNLOAD(":download"),
    HELP(":help"),
    ERROR("Error during file upload/download"),
    HIST_ERROR("No access"),
    HISTORY(":log");
    public final String label;

    private WorkerEventType(String label){
        this.label = label;
    }


}
