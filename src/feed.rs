use article;
#[derive(Debug)]
pub struct Feed {
    name: String,
    url: String,
    art: article::Article,
}

#[cfg(test)]
mod feed_tests {
    #[test]
    fn first_feed_test() {
        assert!(true);
    }
}
