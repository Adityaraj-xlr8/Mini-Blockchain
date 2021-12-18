import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

class Block extends SHA{

    int index;
    int nonce;
    String Amount;
    String timeStamp;
    String hash;
    String prevHash;

    Block(int index,String timeStamp,String prevHash) throws NoSuchAlgorithmException {
        this.index=index;
        Amount="10";
        this.timeStamp=timeStamp;
        this.prevHash=prevHash;
        this.hash=calculateHash();
        this.nonce=0;
    }

    String calculateHash() throws NoSuchAlgorithmException {
        return toHexString(SHA.getSHA(index+nonce+Amount+timeStamp+prevHash));
    }

    void mineBlock(int difficulty) throws NoSuchAlgorithmException {

        char[] zeros= new char[difficulty];
        Arrays.fill(zeros, '1');
        String  zeroString= new String(zeros);

       while(!this.hash.substring(0,difficulty).equals(zeroString)){
            this.nonce++;
            this.hash=calculateHash();
       }
        System.out.println("Block Mined : "+this.hash);
    }
}

public class BlockChain {
    static int blkNum = 1;
    static ArrayList<Block> chain = new ArrayList<>();
    int difficulty = 5;

    BlockChain() throws NoSuchAlgorithmException {
        chain.add(createGenesisBlock());
    }

    Block createGenesisBlock() throws NoSuchAlgorithmException {
        return new Block(0, "2021-06-25 12:00:00", SHA.toHexString(SHA.getSHA("0")));
    }

    Block getLatestBlock() {
        return chain.get(chain.size() - 1);
    }

    void addBlock(int index, String timeStamp) throws NoSuchAlgorithmException {
        Block newBlock = new Block(index, timeStamp, getLatestBlock().hash);
        newBlock.mineBlock(this.difficulty);
        chain.add(newBlock);
    }

    static boolean isChainValid() throws NoSuchAlgorithmException {
        for (int i = 1; i < chain.size(); i++) {
            final Block currentBlock = chain.get(i);
            final Block previousBlock = chain.get(i - 1);

            if (!(currentBlock.hash).equals(currentBlock.calculateHash())) {
                return false;
            }
            if (!(currentBlock.prevHash).equals(previousBlock.hash)) {
                return false;
            }
        }
        return true;
    }


    public static void main(String[] args) throws NoSuchAlgorithmException {

        BlockChain B = new BlockChain();
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("Enter\n1 : Mine\n2 : Chain_Validity\n3 : All Transactions\n4 : EXIT\n");
            int num = sc.nextInt();
            switch (num) {
                case 1:
                    System.out.println("Mining Block " + blkNum + "......");
                    Date date = Calendar.getInstance().getTime();
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
                    String strDate = dateFormat.format(date);
                    B.addBlock(blkNum, strDate);
                    blkNum++;
                    break;
                case 2:
                    System.out.println("Checking Chain Validity ?");
                    if (isChainValid()) System.out.println("YES!!! Chain is valid");
                    else System.out.println("NO!!! Chain is NOT Valid");
                    break;
                case 3:
                    System.out.println();
                    chain.forEach(chain -> System.out.println(chain.index + " " + chain.Amount + " " + chain.timeStamp + " " + chain.prevHash + " " + chain.hash));
                    break;
                case 4:
                    System.exit(0);
                default:
                    System.out.println("WRONG Input");
            }
        }
    }
}


