package io.github.clearwsd.semlink.aligner;

import java.util.List;
import java.util.stream.Collectors;

import io.github.clearwsd.propbank.type.ArgNumber;
import io.github.clearwsd.semlink.PropBankPhrase;
import io.github.clearwsd.verbnet.type.NounPhrase;
import io.github.clearwsd.verbnet.type.ThematicRoleType;
import lombok.NonNull;

/**
 * Just map remaining numbered args to any remaining valid frame roles.
 *
 * @author jgung
 */
public class FillerAligner implements PbVnAligner {

    @Override
    public void align(@NonNull PbVnAlignment alignment) {

        // TODO: this seems like a hack
        boolean noAgentiveA0 = alignment.proposition().predicate().sense().relatedClasses().stream()
                .allMatch(s -> s.verbClass().getThematicRoles().stream()
                        .map(r -> ThematicRoleType.fromString(r.getType().getID()).orElse(ThematicRoleType.NONE))
                        .noneMatch(ThematicRoleType::isAgentive));

        for (PropBankPhrase phrase : alignment.sourcePhrases(false)) {
            List<NounPhrase> unaligned = alignment.targetPhrases(false).stream()
                    .filter(i -> i instanceof NounPhrase)
                    .map(i -> ((NounPhrase) i))
                    .collect(Collectors.toList());
            if (phrase.getNumber() == ArgNumber.A0) {
                // TODO: seems like a hack
                if (alignment.proposition().predicate().id().startsWith("51") && noAgentiveA0) {
                    for (NounPhrase unalignedPhrase : unaligned) {
                        if (unalignedPhrase.thematicRoleType() == ThematicRoleType.THEME) {
                            alignment.add(phrase, unalignedPhrase);
                            break;
                        }
                    }
                } else {
                    for (NounPhrase unalignedPhrase : unaligned) {
                        if (unalignedPhrase.thematicRoleType() == ThematicRoleType.AGENT) {
                            alignment.add(phrase, unalignedPhrase);
                            break;
                        }
                    }
                }
            } else if (phrase.getNumber() == ArgNumber.A1) {
                for (NounPhrase unalignedPhrase : unaligned) {
                    if (unalignedPhrase.thematicRoleType() == ThematicRoleType.THEME
                            || unalignedPhrase.thematicRoleType() == ThematicRoleType.PATIENT) {
                        alignment.add(phrase, unalignedPhrase);
                        break;
                    }
                }
            } else if (phrase.getNumber() == ArgNumber.A3) {
                for (NounPhrase unalignedPhrase : unaligned) {
                    if (unalignedPhrase.thematicRoleType().isStartingPoint()) {
                        alignment.add(phrase, unalignedPhrase);
                        break;
                    }
                }
            } else if (phrase.getNumber() == ArgNumber.A4) {
                for (NounPhrase unalignedPhrase : unaligned) {
                    if (unalignedPhrase.thematicRoleType().isEndingPoint()) {
                        alignment.add(phrase, unalignedPhrase);
                        break;
                    }
                }
            }


        }
    }

}
