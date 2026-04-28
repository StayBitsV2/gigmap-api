package com.staybits.gigmapapi.communities.application.internal.commandservices;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.staybits.gigmapapi.authentication.domain.model.aggregates.User;
import com.staybits.gigmapapi.authentication.infrastructure.persistence.jpa.repositories.UserRepository;
import com.staybits.gigmapapi.communities.domain.model.aggregates.Community;
import com.staybits.gigmapapi.communities.domain.model.commands.CreateCommunityCommand;
import com.staybits.gigmapapi.communities.domain.model.commands.DeleteCommunityCommand;
import com.staybits.gigmapapi.communities.domain.model.commands.JoinCommunityCommand;
import com.staybits.gigmapapi.communities.domain.model.commands.LeaveCommunityCommand;
import com.staybits.gigmapapi.communities.domain.model.commands.UpdateCommunityCommand;
import com.staybits.gigmapapi.communities.domain.services.CommunityCommandService;
import com.staybits.gigmapapi.communities.infrastructure.persistence.jpa.repositories.CommunityRepository;

@Service
public class CommunityCommandServiceImpl implements CommunityCommandService {
    private final CommunityRepository communityRepository;
    private final UserRepository userRepository;

    public CommunityCommandServiceImpl(CommunityRepository communityRepository, UserRepository userRepository) {
        this.communityRepository = communityRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Community handle(CreateCommunityCommand command) {
        if (communityRepository.existsByName(command.name()))
            throw new IllegalArgumentException("Community with name " + command.name() + " already exists");

        var community = new Community(command);

        try {
            communityRepository.save(community);
        } catch (Exception e){
            throw new IllegalArgumentException("Failed to create community " + e.getMessage());
        }

        return community;
    }

    @Override
    public Optional<Community> handle(UpdateCommunityCommand command) {
        Optional<Community> optionalCommunity = communityRepository.findById(command.id());

        if (optionalCommunity.isPresent()) {
            Community community = optionalCommunity.get();

            community.setName(command.name());
            community.setDescription(command.description());
            community.setUpdatedAt(LocalDateTime.now());

            try {
                communityRepository.save(community);
            } catch (Exception e){
                throw new IllegalArgumentException("Failed to update community " + e.getMessage());
            }

            return Optional.of(community);
        }

        return Optional.empty();
    }

    @Override
    public void handle(DeleteCommunityCommand command) {
        Optional<Community> optionalCommunity = communityRepository.findById(command.id());

        if (optionalCommunity.isPresent()) {
            try {
                communityRepository.deleteById(command.id());
            } catch (Exception e) {
                throw new IllegalArgumentException("Failed to delete community: " + e.getMessage());
            }
        } else {
            throw new IllegalArgumentException("Community with ID " + command.id() + " was not found.");
        }
    }

    @Override
    public void handle(JoinCommunityCommand command) {
        User user = userRepository.findById(command.userId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Community community = communityRepository.findById(command.communityId())
                .orElseThrow(() -> new RuntimeException("Community not found"));

        boolean alreadyMember = community.getMembers()
                .stream()
                .anyMatch(member -> member.getId().equals(user.getId()));

        if (alreadyMember) {
            throw new IllegalArgumentException("User is already a member of this community");
        }

        community.getMembers().add(user);
        user.getCommunitiesJoined().add(community);

        try {
            communityRepository.save(community);
            userRepository.save(user);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to join community: " + e.getMessage());
        }
    }

    @Override
    public void handle(LeaveCommunityCommand command) {
        User user = userRepository.findById(command.userId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Community community = communityRepository.findById(command.communityId())
                .orElseThrow(() -> new RuntimeException("Community not found"));

        boolean isMember = community.getMembers()
                .stream()
                .anyMatch(member -> member.getId().equals(user.getId()));

        if (!isMember) {
            throw new IllegalArgumentException("User is not a member of this community");
        }
        
        community.getMembers().remove(user);
        user.getCommunitiesJoined().remove(community);

        try {
            communityRepository.save(community);
            userRepository.save(user);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to leave community: " + e.getMessage());
        }
    }
}
