package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.yearup.data.ProfileDao;
import org.yearup.data.UserDao;
import org.yearup.models.Profile;
import org.yearup.models.User;

import java.security.Principal;

@RestController
@RequestMapping("/profile")
@CrossOrigin
@PreAuthorize("isAuthenticated()")
public class ProfileController
{
    private ProfileDao profileDao;
    private UserDao userDao;

    @Autowired
    public ProfileController(ProfileDao profileDao, UserDao userDao)
    {
        this.profileDao = profileDao;
        this.userDao = userDao;
    }

    /**
     * GET /profile
     * Retrieves the current user's profile
     * No request body required
     * Returns the user's profile information
     */
    @GetMapping
    public ResponseEntity<Profile> getProfile(Principal principal)
    {
        try
        {
            // Get the current user's username from the Principal
            String username = principal.getName();
            
            // Find the user in the database
            User user = userDao.getByUserName(username);
            if (user == null)
            {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
            }

            // Get the user's profile
            Profile profile = profileDao.getByUserId(user.getId());
            if (profile == null)
            {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile not found for user");
            }

            return ResponseEntity.ok(profile);
        }
        catch (ResponseStatusException ex)
        {
            throw ex;
        }
        catch (Exception ex)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving profile: " + ex.getMessage());
        }
    }

    /**
     * PUT /profile
     * Updates the current user's profile
     * Requires Profile object in request body
     * Returns the updated profile
     */
    @PutMapping
    public ResponseEntity<Profile> updateProfile(Principal principal, @RequestBody Profile profile)
    {
        try
        {
            // Get the current user's username from the Principal
            String username = principal.getName();
            
            // Find the user in the database
            User user = userDao.getByUserName(username);
            if (user == null)
            {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
            }

            // Validate that the profile belongs to the current user
            if (profile.getUserId() != user.getId())
            {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Cannot update another user's profile");
            }

            // Validate required fields
            if (profile.getFirstName() == null || profile.getFirstName().trim().isEmpty())
            {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "First name is required");
            }
            if (profile.getLastName() == null || profile.getLastName().trim().isEmpty())
            {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Last name is required");
            }
            if (profile.getEmail() == null || profile.getEmail().trim().isEmpty())
            {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is required");
            }
            if (profile.getPhone() == null || profile.getPhone().trim().isEmpty())
            {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Phone is required");
            }
            if (profile.getAddress() == null || profile.getAddress().trim().isEmpty())
            {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Address is required");
            }
            if (profile.getCity() == null || profile.getCity().trim().isEmpty())
            {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "City is required");
            }
            if (profile.getState() == null || profile.getState().trim().isEmpty())
            {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "State is required");
            }
            if (profile.getZip() == null || profile.getZip().trim().isEmpty())
            {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Zip code is required");
            }

            // Check if profile exists
            Profile existingProfile = profileDao.getByUserId(user.getId());
            if (existingProfile == null)
            {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile not found for user");
            }

            // Update the profile
            profileDao.update(profile);

            // Return the updated profile
            Profile updatedProfile = profileDao.getByUserId(user.getId());
            return ResponseEntity.ok(updatedProfile);
        }
        catch (ResponseStatusException ex)
        {
            throw ex;
        }
        catch (Exception ex)
        {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error updating profile: " + ex.getMessage());
        }
    }
} 