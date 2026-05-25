package unihousie.controller;

import unihousie.entity.LifestyleProfile;
import unihousie.entity.User;
import unihousie.entity.UserSummary;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class MatchmakingController {

    public List<LifestyleProfile> searchCompatibleRoommates(String studentId, Map<String, Object> filters) {
        if (studentId == null || studentId.isEmpty()) {
            throw new IllegalArgumentException("studentId δεν μπορεί να είναι κενό.");
        }
        LifestyleProfile ownProfile = LifestyleProfile.findOwnProfile(studentId);
        if (ownProfile == null || !ownProfile.isCompleted()) {

            return Collections.emptyList();
        }
        return LifestyleProfile.queryMatches(filters, ownProfile);
    }

    public UserSummary getRoommateCardData(String targetUserId) {
        if (targetUserId == null || targetUserId.isEmpty()) {
            throw new IllegalArgumentException("targetUserId δεν μπορεί να είναι κενό.");
        }
        return User.getUserSummaryDetails(targetUserId);
    }
}
