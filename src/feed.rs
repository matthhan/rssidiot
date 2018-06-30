use article::Article;
use circular_buffer::CircularBuffer;
#[derive(Debug)]
pub struct Feed {
    name: String,
    url: String,
    articles: CircularBuffer<Article>,
}

#[cfg(test)]
mod feed_tests {
    #[test]
    fn first_feed_test() {
        assert!(true);
    }
}
