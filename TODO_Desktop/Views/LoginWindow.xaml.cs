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
        }
        private void TextBox_GotFocus(object sender, RoutedEventArgs e)
        {
            var tb = sender as TextBox;

            if (tb == UsernameBox && tb.Text == "Username")
            {
                tb.Text = "";
                tb.Foreground = Brushes.Black;
            }

            if (tb == EmailBox && tb.Text == "Email")
            {
                tb.Text = "";
                tb.Foreground = Brushes.Black;
            }
        }

        private void TextBox_LostFocus(object sender, RoutedEventArgs e)
        {
            var tb = sender as TextBox;

            if (string.IsNullOrWhiteSpace(tb.Text))
            {
                if (tb == UsernameBox)
                {
                    tb.Text = "Username";
                    tb.Foreground = Brushes.Gray;
                }

                if (tb == EmailBox)
                {
                    tb.Text = "Email";
                    tb.Foreground = Brushes.Gray;
                }
            }
        }


        private async void Login_Click(object sender, RoutedEventArgs e)
        {
            var response = await _apiClient.LoginAsync(UsernameBox.Text, PasswordBox.Password);

            if (!response)
            {
                // Получение сообщения об ошибке из последнего ответа
                var errorContent = await _apiClient.GetLastErrorMessageAsync();
                MessageBox.Show(!string.IsNullOrEmpty(errorContent) ? errorContent : "Login failed.", "Error", MessageBoxButton.OK, MessageBoxImage.Error);
                /*StatusBlock.Text = !string.IsNullOrEmpty(errorContent) ? errorContent : "Login failed.";*/
                return;
            }
            /*MessageBox.Show("Login Successful");*/
            /*StatusBlock.Text = "Login successful!"*/;
            var mainWindow = new MainWindow(_apiClient);
            mainWindow.Show();
            this.Close();
        }

        private async void Register_Click(object sender, RoutedEventArgs e)
        {
            
            Console.WriteLine($"Request Data: Username={UsernameBox.Text}, Email={EmailBox.Text}, Password={PasswordBox.Password}");
            bool result = await _apiClient.RegisterAsync(UsernameBox.Text, EmailBox.Text, PasswordBox.Password);
            
            if (!result)
            {
                // Получаем сообщение об ошибке из последнего ответа
                var errorContent = await _apiClient.GetLastErrorMessageAsync();
                MessageBox.Show(!string.IsNullOrEmpty(errorContent) ? errorContent : "Registration failed.", "Error", MessageBoxButton.OK, MessageBoxImage.Error);
                /*StatusBlock.Text = !string.IsNullOrEmpty(errorContent) ? errorContent : "Registration failed.";*/
                return;
            }
            MessageBox.Show("Registration Successful");
            /*StatusBlock.Text = "Registration successful!";*/
        }
    }
}