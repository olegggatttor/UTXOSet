import UTXOSet.ElementProof;
import UTXOSet.UTXOSetImpl;
import UTXOSet.UTXOSetProof;
import UTXOSet.UTXOSetProofImpl;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;

import static org.junit.Assert.*;

public class SimpleUTXOSetProofImplTest extends SimpleUTXOSetBaseTest {
    private UTXOSetProof utxoSetProof;
    private static ArrayList<String> coins;
    private static final int AMOUNT_OF_COINS = 100;
    private final int AMOUNT_OF_TEST = 100;

    @BeforeClass
    public static void generateCoins() {
        coins = new ArrayList<>();
        for (int i = 0; i != AMOUNT_OF_COINS; ++i) {
            coins.add(Node.genCoin());
        }
    }

    @Before
    public void init() {
        try {
            utxoSet = new UTXOSetProofImpl();
            utxoSetProof = new UTXOSetProofImpl();
        } catch (NoSuchAlgorithmException ignore) {
            fail();
        }
    }

    @Test
    public void saveAndGetProof() {
        try {
            for (int i = 0; i < AMOUNT_OF_TEST; i++) {
                utxoSet = new UTXOSetImpl();
                utxoSetProof = new UTXOSetProofImpl();

                Collections.shuffle(coins);
                for (String coin : coins) {
                    utxoSetProof.addAndSave(coin);
                    utxoSet.add(coin);
                }

                Collections.shuffle(coins);
                for (String coin : coins) {
                    final ElementProof proof = utxoSetProof.getProof(coin);

                    assertTrue(utxoSetProof.verify(proof));
                    assertTrue(utxoSetProof.verifyAndDelete(proof));
                    assertFalse(utxoSetProof.verifyAndDelete(proof));

                    assertTrue(utxoSet.verify(proof));
                    assertTrue(utxoSet.verifyAndDelete(proof));
                    assertFalse(utxoSet.verifyAndDelete(proof));
                }
            }

        } catch (NoSuchAlgorithmException ignore) {
            fail();
        }
    }
}
