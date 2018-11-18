use application::ApplicationState;


pub fn get_action_for(x:char) -> fn(ApplicationState) -> ApplicationState {
    if x == 'j' {
        |state| state.article_cursor_down()
    } else if x == 'k' {
        |state| state.article_cursor_up()
    } else if x == 'f' {
        |state| state.feed_cursor_down()
    } else if x == 'd' {
        |state| state.feed_cursor_up()
    } else {
        identity
    }
}
fn identity(state: ApplicationState) -> ApplicationState {
    state
}

