package studijosKTU;

/** @author Eimutis Karčiauskas, KTU IF Programų inžinerijos katedra, 2014 09 23
 *
 * Tai yra  interfeisas, kurį turi tenkinti KTU studentų kuriamos duomenų klasės
 *       Metodai užtikrina patogų duomenų suformavimą iš String eilučių
 ******************************************************************************/
public interface KTUable {

    /**
     * Sukuria naują objektą iš eilutės
     *
     * @param dataString
     * @return
     */
    KTUable create(String dataString);

    /**
     * Patikrina objekto reikšmes
     *
     * @return
     */
    String validate();

    /**
     * Suformuoja objektą iš eilutės
     *
     * @param e
     */
    void parse(String e);
    /**
     * Atvaizduoja objektą į String eilutę
     *
     * @return
     */
    @Override
    String toString();
}