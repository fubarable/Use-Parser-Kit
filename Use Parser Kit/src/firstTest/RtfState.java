package firstTest;

public enum RtfState {
    // ..
    PARAGRAPH("par", StateCategory.PARAGRAPH),
    // ..
    PARAGRAPH_DEFAULT("pard", StateCategory.PARAGRAPH),
    // ..
    HIGHLIGHT("highlight", StateCategory.FORMATTING),
    // ..
    BOLD("b", StateCategory.FORMATTING),
    // ..
    ITALIC("i", StateCategory.FORMATTING),
    // end of a nested table cell
    NEST_CELL("nestcell", StateCategory.TABLE),
    // end of a nested table row
    NEST_ROW("nestrow", StateCategory.TABLE),
    // nested table properties
    NEST_TABLE_PROPS("nesttableprops", StateCategory.TABLE),
    // table row defaults
    TROWD("trowd", StateCategory.TABLE),
    // paragraph in table
    INTBL("intbl", StateCategory.TABLE), // paragraph in table
    // TODO: more
    ;
    String command;
    StateCategory stateCategory;

    private RtfState(String command, StateCategory stateCategory) {
	this.command = command;
	this.stateCategory = stateCategory;
    }

    public String getCommand() {
	return command;
    }

    public StateCategory isFormatting() {
	return stateCategory;
    }

}
