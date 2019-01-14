require 'json'
package = JSON.parse(File.read(File.join(__dir__, './', 'package.json')))

Pod::Spec.new do |s|
  s.name          = package['name']
  s.version       = package['version']
  s.summary       = package['description']
  s.requires_arc  = true
  s.author        = { 'andy' => 'andy@10levels.com' }
  s.license       = package['license']
  s.homepage      = package['homepage']
  s.source        = { :git => 'https://github.com/10levels/react-native-appmetrica.git' }
  s.platform      = :ios, '9.0'
  s.dependency      'React'

  s.subspec 'AppMetrica' do |ss|
    ss.dependency     'YandexMobileMetrica'
    ss.source_files = 'ios/RCTAppMetrica/RCTAppMetrica/*.{h,m}'
  end
end
