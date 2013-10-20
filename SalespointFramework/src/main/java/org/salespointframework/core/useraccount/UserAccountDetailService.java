package org.salespointframework.core.useraccount;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

//http://docs.spring.io/spring-security/site/docs/3.2.x/reference/html/technical-overview.html
public class UserAccountDetailService implements UserDetailsService {

	private UserAccountManager userAccountManager;

	public UserAccountDetailService(UserAccountManager userAccountManager) {
		this.userAccountManager = userAccountManager;
	}

	@Override
	public UserDetails loadUserByUsername(String name)
			throws UsernameNotFoundException {
		UserAccountIdentifier userAccountIdentifier = new UserAccountIdentifier(
				name);
		UserAccount userAccount = userAccountManager.get(userAccountIdentifier);

		if (userAccount == null)
			throw new UsernameNotFoundException("Useraccount: " + name
					+ "not found");

		return new UserAccountDetails(userAccount);
	}

	@SuppressWarnings("serial")
	private class UserAccountDetails implements UserDetails {

		private final String username;
		private final String password;
		private final List<GrantedAuthority> authorities = new LinkedList<>();

		public UserAccountDetails(UserAccount userAccount) {
			this.username = userAccount.getIdentifier().toString();
			this.password = userAccount.getPassword().toString();

			for (Role role : userAccount.getRoles()) {
				authorities.add(new SimpleGrantedAuthority(role.getName()));
			}

		}

		@Override
		public Collection<? extends GrantedAuthority> getAuthorities() {
			return authorities;
		}

		@Override
		public String getPassword() {
			return password;
		}

		@Override
		public String getUsername() {
			return username;
		}

		@Override
		public boolean isAccountNonExpired() {
			return true;
		}

		@Override
		public boolean isAccountNonLocked() {
			return true;
		}

		@Override
		public boolean isCredentialsNonExpired() {
			return true;
		}

		@Override
		public boolean isEnabled() {
			return true;
		}

		@Override
		public String toString() {
			return username;
		}

		@Override
		public int hashCode() {
			return username.hashCode();
		}

		@Override
		public final boolean equals(Object other) {
			if (other == null) {
				return false;
			}
			if (other == this) {
				return true;
			}
			if (other instanceof UserAccountDetails) {
				return this.username
						.equals(((UserAccountDetails) other).username);
			}
			return false;
		}

	}

}
