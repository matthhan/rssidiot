use application::ApplicationState;
use feed::Feed;
use article::Article;
pub fn construct_example_state() -> ApplicationState {
    let state = ApplicationState::new()
        .add_feed(
            Feed::new("Example news".to_string(), "example.com".to_string())
                .add_article(Article::new("hello world".to_string(), "example.com/art/1".to_string()))
                .add_article(Article::new("hello world2".to_string(), "example.com/art/2".to_string()))
            )
        .add_feed(
            Feed::new("Example news2".to_string(), "example.com".to_string())
                .add_article(Article::new("hello world3".to_string(), "example.com/art/1".to_string()))
                .add_article(Article::new("hello world4".to_string(), "example.com/art/2".to_string()))
            );
    state
}
