use article::Article;
use circular_buffer::CircularBuffer;




#[derive(Debug,Serialize,Deserialize,PartialEq,Eq)]
pub struct Feed {
    name: String,
    url: String,
    articles: CircularBuffer<Article>,
}
impl Feed {
    pub fn new(name:String,url:String) -> Feed {
        let articles: CircularBuffer<Article> = CircularBuffer::new(100);
        Feed {
            name,
            url,
            articles,
        }
    }
}
#[cfg(test)]
mod feed_tests {
    use feed::Feed;
    extern crate serde_json;
    #[test]
    fn can_create() {
        let fe = Feed::new("Example Feed".to_string(), "www.example.com".to_string());
        assert!(true)
    }
    fn can_serialize_and_deserialize() {
        let fe = Feed::new("Example Feed".to_string(), "www.example.com".to_string());
        let as_str = serde_json::to_string(&fe).unwrap(); 
        let from_str = serde_json::from_str(&as_str).unwrap();
        assert_eq!(fe,from_str)
    }
}
