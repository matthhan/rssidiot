
use feed::Feed;
use cursor::Cursor;
use can_replace_at_index::CanReplaceAtIndex;

#[derive(Debug)]
pub struct ApplicationState {
    feeds: Vec<Feed>,
    feeds_cursor: Cursor,
    articles_cursor: Cursor,
}
impl ApplicationState {
    pub fn new() -> ApplicationState {
        ApplicationState {
            feeds: Vec::new(),
            feeds_cursor: Cursor::new(0),
            articles_cursor: Cursor::new(0),
        }
    }
    pub fn article_cursor_down(self) -> ApplicationState {
        if self.feeds_cursor.position.is_some() {
            let changed_cursor = ApplicationState { 
                articles_cursor:self.articles_cursor.move_down(), 
                ..self
            };
            changed_cursor.set_current_article_read()
        } else {
            self
        }
    }
    pub fn article_cursor_up(self) -> ApplicationState {
        if self.feeds_cursor.position.is_some() {
            let changed_cursor = ApplicationState { 
                articles_cursor:self.articles_cursor.move_up(), 
                ..self
            };
            changed_cursor.set_current_article_read()
        } else {
            self
        }
    }
    pub fn feed_cursor_up(self) -> ApplicationState {
        let nfc = self.feeds_cursor.move_up();
        let nac = if nfc.position.is_some() {
            Cursor::new(self.feeds[nfc.position.unwrap()].len())
        } else {
            self.articles_cursor
        };
        ApplicationState {
            feeds_cursor: nfc,
            articles_cursor: nac,
            ..self
        }
    }
    pub fn feed_cursor_down(self) -> ApplicationState {
        let nfc = self.feeds_cursor.move_down();
        let nac = if nfc.position.is_some() {
            Cursor::new(self.feeds[nfc.position.unwrap()].len())
        } else {
            self.articles_cursor
        };
        ApplicationState {
            feeds_cursor: nfc,
            articles_cursor: nac,
            ..self
        }
    }
    fn set_current_article_read(self) -> ApplicationState {
        if self.feeds_cursor.position.is_some() && self.articles_cursor.position.is_some() {
            let ac = self.articles_cursor.position.unwrap();
            let fc = self.feeds_cursor.position.unwrap();
            ApplicationState { 
                feeds:
                    self.feeds.replace_at_index(fc, &|o_feed:Option<Feed>| -> Feed { o_feed.unwrap().set_article_read(ac)}) , 
                ..self
            }
        } else {
            self
        }
    }
}




#[cfg(test)]
mod application_tests {
    use application::ApplicationState;
    #[test]
    fn without_feeds_navigating_never_gives_position() {
        let app = ApplicationState::new();
        let app = app.article_cursor_down();
        let app = app.article_cursor_up();
        let app = app.feed_cursor_down();
        let app = app.feed_cursor_up();
        assert!(app.articles_cursor.position.is_none());
        assert!(app.feeds_cursor.position.is_none());
    }
}
