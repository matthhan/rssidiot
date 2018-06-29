
#[derive(Debug)]
pub struct CircularBuffer<T> where T:Clone {
    
    last_inserted: usize,
    backing_structure:Vec<T>,
    size:usize,
}
impl<T> CircularBuffer<T>  where T:Clone {
    pub fn new(size:usize) -> CircularBuffer<T> {
        CircularBuffer {
            size,
            last_inserted: size - 1,
            backing_structure: Vec::new(),
        }
    }
    pub fn add(self,thing: T) -> CircularBuffer<T> {
        let last_inserted_new = (self.last_inserted + 1) % self.size;
        let backing_structure_new = CircularBuffer::place_in_vec(self.backing_structure,last_inserted_new,thing);
        CircularBuffer {
            size: self.size,
            last_inserted: last_inserted_new,
            backing_structure: backing_structure_new,
        }
    }
    fn place_in_vec(vec:Vec<T>,ind:usize,thing:T) -> Vec<T> {
       let mut newvec = vec.to_vec(); 
        if ind == newvec.len() {
            newvec.push(thing)
        } else {
            newvec[ind] = thing
        }
        newvec
    }
}
impl<T> IntoIterator for CircularBuffer<T> where T:Clone {
    type Item = T;
    type IntoIter = CircularBufferIterator<T>;
    fn into_iter(self) -> Self::IntoIter {
        CircularBufferIterator {
            i: 0,
            buf: self,
        }
    }
}
pub struct CircularBufferIterator<T> where T:Clone {
    buf: CircularBuffer<T>,
    i: usize,
}
impl<T> Iterator for CircularBufferIterator<T> where T:Clone {
    type Item = T;
    fn next(&mut self) -> Option<T> where T:Clone {
        if self.i < self.buf.backing_structure.len() {
            let res = Some(self.buf.backing_structure[self.i].clone());
            self.i += 1;
            res
        } else {
            None
        }
    }
}

#[cfg(test)]
mod circular_buffer_tests {
    use circular_buffer::CircularBuffer;
    #[test]
    fn can_create() {
        let buf:CircularBuffer<i32> = CircularBuffer::new(100);
    }
    #[test]
    fn can_iterate() {
        let buf:CircularBuffer<i32> = CircularBuffer::new(100);
        for i in buf {
            println!("{}",i)
        }
    }
    #[test]
    fn can_add() {
        let buf:CircularBuffer<i32> = CircularBuffer::new(100);
        let newbuf = buf.add(3);
        println!("{:?}",newbuf); 
    }
}
