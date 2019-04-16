package io.github.semlink.parser;

import io.github.clearwsd.verbnet.VnClass;
import io.github.semlink.propbank.type.PropBankArg;
import io.github.semlink.verbnet.semantics.SemanticPredicate;
import io.github.semlink.verbnet.type.ThematicRoleType;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * VerbNet proposition.
 *
 * @author jgung
 */
@Setter
@Getter
@Accessors(fluent = true)
public class VerbNetProp {

    private int tokenIndex;
    private List<String> tokens;
    private List<SemanticPredicate> predicates = new ArrayList<>();
    private Proposition<VnClass, PropBankArg> propbankProp;
    private Proposition<VnClass, ThematicRoleType> verbnetProp;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(" ----------- PropBank Semantic Roles -------- \n");
        sb.append(propbankProp.toString(tokens)).append("\n");
        if (null != verbnetProp) {
            sb.append(" ----------- VerbNet Roles -------- \n");
            sb.append(verbnetProp.toString(tokens)).append("\n");
        }
        if (!predicates.isEmpty()) {
            sb.append(" ----------- Semantic Analysis ----- \n");
            predicates.forEach(p -> sb.append(p).append("\n"));
        }
        return sb.toString();
    }
}