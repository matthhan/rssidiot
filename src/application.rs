
use feed::Feed;
use cursor::Cursor;

#[derive(Debug)]
pub struct ApplicationState {
    feeds: Vec<Feed>,
    feeds_cursor: Cursor,
    articles_cursor: Cursor,
}
impl ApplicationState {
    /*fn article_cursor_down(self) {
        let new_curs = self.articles_cursor.move_down();
        let new_article = self.feeds[feeds_cursor].articles[new_curs].set_read
        let new_feeds = self.feeds.replace_at(feeds_cursor,self.feeds[feeds_cursor].articles.replace_at(new_curs,new_article))
        { articles_cursor.move_down(), ..self }
    }*/
}
