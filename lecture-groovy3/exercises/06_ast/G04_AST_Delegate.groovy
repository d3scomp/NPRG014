class Event {
    String name
    @Delegate List attendees
}

return new Event()

//TASK Explore the class in Ast Inspector and explain the effect of the annotation