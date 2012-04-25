import pertinence.cognibox.huffman.*

class Gestures {

	void run() {
		Map concepts = [ 
		                 'N'		: 1000,
		                 'D'		: 1000,
		                 '('		: 999,
		                 ')'		: 999,
		                 'FFFFF' 	: 3,
		                 'LFF'		: 2,
		                 'F'		: 1
		] 
		HuffmanTree tree = new HuffmanTree(concepts)
		tree.totallyUnportableSeeGraphvizRepresentation()
	}
}
//'FFFFF LFLLFLLLLFFLLFLF'

new Gestures().run()
