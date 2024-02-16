package com.example.playfordapp;

public class Event {
    private long Id;
    private long EventID;
    private int ModuleInstanceID;
    private boolean RecurringEvent;
    private boolean RegisteredUsers;
    private String Title;
    private String Start;
    private String End;
    private String CategoryColor;
    private String CategoryTitle;
    private String EventSource;
    private String RecurringInfo;
    private boolean AllDay;

    @Override
    public String toString() {
        return "Event{" +
                "Id=" + Id +
                ", EventID=" + EventID +
                ", ModuleInstanceID=" + ModuleInstanceID +
                ", RecurringEvent=" + RecurringEvent +
                ", RegisteredUsers=" + RegisteredUsers +
                ", Title='" + Title + '\'' +
                ", Start='" + Start + '\'' +
                ", End='" + End + '\'' +
                ", CategoryColor='" + CategoryColor + '\'' +
                ", CategoryTitle='" + CategoryTitle + '\'' +
                ", EventSource='" + EventSource + '\'' +
                ", RecurringInfo='" + RecurringInfo + '\'' +
                ", AllDay=" + AllDay +
                ", NoEndTime=" + NoEndTime +
                '}';
    }

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public long getEventID() {
        return EventID;
    }

    public void setEventID(long eventID) {
        EventID = eventID;
    }

    public int getModuleInstanceID() {
        return ModuleInstanceID;
    }

    public void setModuleInstanceID(int moduleInstanceID) {
        ModuleInstanceID = moduleInstanceID;
    }

    public boolean isRecurringEvent() {
        return RecurringEvent;
    }

    public void setRecurringEvent(boolean recurringEvent) {
        RecurringEvent = recurringEvent;
    }

    public boolean isRegisteredUsers() {
        return RegisteredUsers;
    }

    public void setRegisteredUsers(boolean registeredUsers) {
        RegisteredUsers = registeredUsers;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getStart() {
        return Start;
    }

    public void setStart(String start) {
        Start = start;
    }

    public String getEnd() {
        return End;
    }

    public void setEnd(String end) {
        End = end;
    }

    public String getCategoryColor() {
        return CategoryColor;
    }

    public void setCategoryColor(String categoryColor) {
        CategoryColor = categoryColor;
    }

    public String getCategoryTitle() {
        return CategoryTitle;
    }

    public void setCategoryTitle(String categoryTitle) {
        CategoryTitle = categoryTitle;
    }

    public String getEventSource() {
        return EventSource;
    }

    public void setEventSource(String eventSource) {
        EventSource = eventSource;
    }

    public String getRecurringInfo() {
        return RecurringInfo;
    }

    public void setRecurringInfo(String recurringInfo) {
        RecurringInfo = recurringInfo;
    }

    public boolean isAllDay() {
        return AllDay;
    }

    public void setAllDay(boolean allDay) {
        AllDay = allDay;
    }

    public int getNoEndTime() {
        return NoEndTime;
    }

    public void setNoEndTime(int noEndTime) {
        NoEndTime = noEndTime;
    }

    private int NoEndTime;

}

