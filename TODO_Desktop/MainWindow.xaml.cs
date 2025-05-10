using System.Text;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;
using TODO_Desktop.Api;
namespace TODO_Desktop;

/// <summary>
/// Interaction logic for MainWindow.xaml
/// </summary>
public partial class MainWindow : Window
{
    private readonly ApiClient _apiClient;

    public MainWindow(ApiClient apiClient)
    {
        InitializeComponent();
        _apiClient = apiClient;
    }

    private async void Window_Loaded(object sender, RoutedEventArgs e)
    {
        if (!_apiClient.IsAuthorized)
        {
            MessageBox.Show("Not authorized!");
            this.Close();
            return;
        }

        var data = await _apiClient.GetProtectedDataAsync();
        MessageBox.Show("Protected data: " + data);
    }
}