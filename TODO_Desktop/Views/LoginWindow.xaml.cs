using System.Windows;
using System.Windows.Controls;
using System.Windows.Media;
using TODO_Desktop;
using TODO_Desktop.Api;

namespace TODO_Desktop.Views
{
    public partial class LoginWindow : Window
    {
        private readonly ApiClient _apiClient = new ApiClient();

        public LoginWindow()
        {
            InitializeComponent();
            ResizeMode = ResizeMode.NoResize; 
        }
        
        private void RegisterLink_Click(object sender, RoutedEventArgs e)
        {
            LoginPanel.Visibility = Visibility.Collapsed;
            RegisterPanel.Visibility = Visibility.Visible;
        }

        private void LoginLink_Click(object sender, RoutedEventArgs e)
        {
            RegisterPanel.Visibility = Visibility.Collapsed;
            LoginPanel.Visibility = Visibility.Visible;
        }

        private async void Login_Click(object sender, RoutedEventArgs e)
        {
            var email = LoginEmailBox.Text.Trim();
            var password = LoginPasswordBox.Password;

            if (string.IsNullOrEmpty(email) || string.IsNullOrEmpty(password))
            {
                MessageBox.Show("Please enter both email and password.", "Validation", MessageBoxButton.OK, MessageBoxImage.Warning);
                return;
            }

            var success = await _apiClient.LoginAsync(email, password);
            if (!success)
            {
                var error = await _apiClient.GetLastErrorMessageAsync();
                MessageBox.Show(!string.IsNullOrEmpty(error) ? error : "Login failed.", "Error", MessageBoxButton.OK, MessageBoxImage.Error);
                return;
            }

            var main = new MainWindow(_apiClient);
            main.Show();
            Close();
        }

        private async void Register_Click(object sender, RoutedEventArgs e)
        {
            var username = RegUsernameBox.Text.Trim();
            var email    = RegEmailBox.Text.Trim();
            var pwd      = RegPasswordBox.Password;
            var confirm  = RegConfirmPasswordBox.Password;

            if (string.IsNullOrEmpty(username) ||
                string.IsNullOrEmpty(email) ||
                string.IsNullOrEmpty(pwd) ||
                string.IsNullOrEmpty(confirm))
            {
                MessageBox.Show("All fields are required.", "Validation", MessageBoxButton.OK, MessageBoxImage.Warning);
                return;
            }

            if (pwd != confirm)
            {
                MessageBox.Show("Passwords do not match.", "Validation", MessageBoxButton.OK, MessageBoxImage.Warning);
                return;
            }

            var result = await _apiClient.RegisterAsync(username, email, pwd);
            if (!result)
            {
                var error = await _apiClient.GetLastErrorMessageAsync();
                MessageBox.Show(!string.IsNullOrEmpty(error) ? error : "Registration failed.", "Error", MessageBoxButton.OK, MessageBoxImage.Error);
                return;
            }

            MessageBox.Show("Registration successful!", "Success", MessageBoxButton.OK, MessageBoxImage.Information);
            // Можно автоматически переключить на вкладку Login:
            /*LoginTabControl.SelectedIndex = 0;*/
            var main = new MainWindow(_apiClient);
            main.Show();
            Close();
        }
    }
}